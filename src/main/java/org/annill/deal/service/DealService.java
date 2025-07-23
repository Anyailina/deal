package org.annill.deal.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.annill.deal.converter.DealConverter;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.entity.Deal;
import org.annill.deal.entity.DealContractor;
import org.annill.deal.entity.DealStatus;
import org.annill.deal.entity.DealSum;
import org.annill.deal.filter.DealSearchFilterDto;
import org.annill.deal.repository.DealRepository;
import org.annill.deal.spec.DealSpecifications;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Предоставляет методы для смены статуса сделки, поиска, сохранения и экспорта в Excel.
 */
@Service
@AllArgsConstructor
public class DealService {

    private DealStatusService dealStatusService;
    private DealRepository dealRepository;
    private DealConverter dealConverter;

    /**
     * Меняет статус сделки по идентификатору.
     *
     * @param dealId    идентификатор сделки.
     * @param newStatus новое имя статуса.
     */
    @Transactional
    public Optional<DealDto> changeStatus(UUID dealId, String newStatus) {
        return dealRepository.findById(dealId)
            .flatMap(deal -> dealStatusService.findByName(newStatus)
                .map(status -> {
                    deal.setStatus(status);
                    Deal savedDeal = dealRepository.save(deal);
                    return dealConverter.toDto(savedDeal);
                })
            );
    }

    /**
     * Получает сделку по идентификатору.
     *
     * @param id идентификатор сделки.
     */
    public Optional<DealDto> getById(UUID id) {
        return dealRepository.findById(id)
            .filter(Deal::isActive)
            .map(dealConverter::toDto);
    }

    /**
     * Выполняет поиск сделок по фильтру с постраничной выдачей.
     *
     * @param filter   объект фильтра {@link DealSearchFilterDto}.
     * @param pageable объект пагинации {@link Pageable}.
     */
    public Page<DealDto> searchDeals(DealSearchFilterDto filter, Pageable pageable) {
        Specification<Deal> spec = DealSpecifications.withFilter(filter);
        return dealRepository.findAll(spec, pageable).map(dealConverter::toDto);
    }

    /**
     * Выполняет поиск сделок по фильтру с постраничной выдачей с фильтрацией по ролям.
     *
     * @param filter   объект фильтра {@link DealSearchFilterDto}.
     * @param pageable объект пагинации {@link Pageable}.
     * @param authentication объект Authentication {@link Authentication}.
     */
    public Page<DealDto> searchDeals(DealSearchFilterDto filter, Pageable pageable, Authentication authentication) {

        boolean isCreditUser = hasAuthority(authentication);

        String statusFilter = isCreditUser ? "CREDIT" : "OVERDRAFT";

        Specification<Deal> spec = DealSpecifications.withFilter(filter, statusFilter);
        return dealRepository.findAll(spec, pageable)
            .map(dealConverter::toDto);
    }

    private boolean hasAuthority(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
            .anyMatch(a -> "ROLE_CREDIT_USER".equals(a.getAuthority()));
    }

    /**
     * Экспортирует список сделок в Excel-файл (формат .xlsx) по заданному фильтру и пагинации.
     *
     * @param filter   фильтр поиска сделок.
     * @param pageable параметры пагинации.
     */
    public byte[] exportDeals(DealSearchFilterDto filter, Pageable pageable) throws IOException {
        var spec = DealSpecifications.withFilter(filter);
        var deals = dealRepository.findAll(spec, pageable).getContent();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Deals");
            int rowIdx = 0;

            // Заголовки
            Row headerRow = sheet.createRow(rowIdx++);
            headerRow.createCell(0).setCellValue("ИД сделки");
            headerRow.createCell(1).setCellValue("Описание");
            headerRow.createCell(2).setCellValue("Номер договора");
            headerRow.createCell(3).setCellValue("Дата договора");
            headerRow.createCell(4).setCellValue("Дата и время вступления соглашения в силу");
            headerRow.createCell(5).setCellValue("Срок действия сделки");
            headerRow.createCell(6).setCellValue("Тип сделки");
            headerRow.createCell(7).setCellValue("Статус сделки");
            headerRow.createCell(8).setCellValue("Сумма сделки");
            headerRow.createCell(9).setCellValue("Наименование валюты");
            headerRow.createCell(10).setCellValue("Основная сумма сделки");
            headerRow.createCell(11).setCellValue("Наименование контрагента");
            headerRow.createCell(12).setCellValue("ИНН контрагента");
            headerRow.createCell(13).setCellValue("Роли контрагента");

            for (Deal deal : deals) {
                Row dealRow = sheet.createRow(rowIdx++);
                fillDealMainFields(dealRow, deal);

                rowIdx++;

                if (!deal.getDealSumList().isEmpty()) {
                    for (DealSum dealSum : deal.getDealSumList()) {
                        Row sumRow = sheet.createRow(rowIdx++);
                        sumRow.createCell(8).setCellValue(dealSum.getSum() != null ? dealSum.getSum().toString() : "");
                        sumRow.createCell(9)
                            .setCellValue(dealSum.getCurrency() != null ? dealSum.getCurrency().getName() : "");
                        sumRow.createCell(10)
                            .setCellValue(dealSum.getIsMain() != null && dealSum.getIsMain() ? "Да" : "Нет");
                    }
                }

                if (!deal.getDealContractorList().isEmpty()) {
                    for (DealContractor contractor : deal.getDealContractorList()) {
                        Row contractorRow = sheet.createRow(rowIdx++);
                        contractorRow.createCell(11)
                            .setCellValue(contractor.getName() != null ? contractor.getName() : "");
                        contractorRow.createCell(12)
                            .setCellValue(contractor.getInn() != null ? contractor.getInn() : "");
                        contractorRow.createCell(13).setCellValue(
                            contractor.getContractorToRoleList().stream()
                                .map(c -> c.getRole() != null ? c.getRole().getName() : "")
                                .collect(Collectors.joining(", "))
                        );
                    }
                }

                rowIdx++;
            }

            for (int i = 0; i <= 13; i++) {
                sheet.autoSizeColumn(i);
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        }
    }

    /**
     * Заполняет основные поля сделки в строке Excel.
     */
    private void fillDealMainFields(Row row, Deal deal) {
        row.createCell(0).setCellValue(deal.getId() != null ? deal.getId().toString() : "");
        row.createCell(1).setCellValue(deal.getDescription() != null ? deal.getDescription() : "");
        row.createCell(2).setCellValue(deal.getAgreementNumber() != null ? deal.getAgreementNumber() : "");
        row.createCell(3).setCellValue(deal.getAgreementDate() != null ? deal.getAgreementDate().toString() : "");
        row.createCell(4).setCellValue(deal.getAgreementStartDt() != null ? deal.getAgreementStartDt().toString() : "");
        row.createCell(5).setCellValue(deal.getAvailabilityDate() != null ? deal.getAvailabilityDate().toString() : "");
        row.createCell(6).setCellValue(deal.getType() != null ? deal.getType().getName() : "");
        row.createCell(7).setCellValue(deal.getStatus() != null ? deal.getStatus().getName() : "");
    }

    /**
     * Сохраняет новую сделку со статусом DRAFT.
     *
     * @param dealDtoSave DTO для сохранения сделки.
     */
    public DealDtoSave saveDeal(DealDtoSave dealDtoSave) {
        Optional<DealStatus> draftStatusOptional = dealStatusService.findByName("DRAFT");

        if (draftStatusOptional.isPresent()) {
            DealStatus draftStatus = draftStatusOptional.get();
            Deal newDeal = dealConverter.toEntity(dealDtoSave);
            newDeal.setStatus(draftStatus);
            return dealConverter.toSaveDto(dealRepository.save(newDeal));
        }

        throw new EntityNotFoundException("Status DRAFT not found");
    }

}
