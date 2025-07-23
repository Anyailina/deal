package org.annill.deal.controller.ui;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.annill.deal.controller.DealController;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.filter.DealSearchFilterDto;
import org.annill.deal.page.PageResponse;
import org.annill.deal.service.DealService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Основной контроллер для управления сделками через UI. Предоставляет endpoints для работы со сделками.
 */
@RestController
@RequestMapping("ui/deal")
@RequiredArgsConstructor
@Slf4j
public class UiDealController {

    private final DealController dealController;
    private final DealService dealService;

    /**
     * Изменяет статус сделки.
     *
     * @param dealId    ID сделки
     * @param newStatus новый статус
     * @return ResponseEntity с обновленной сделкой
     */
    @PatchMapping("/change/status")
    public ResponseEntity<DealDto> changeDealStatus(@RequestParam UUID dealId,
        @RequestParam String newStatus) {
        log.info("Изменяет статус сделки. Сделка ID: {}, Статус: {}", dealId, newStatus);
        return dealController.changeDealStatus(dealId, newStatus);
    }

    /**
     * Получает сделку по ID.
     *
     * @param id ID сделки
     * @return ResponseEntity с найденной сделкой
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','CREDIT_USER', 'OVERDRAFT_USER')")
    public ResponseEntity<DealDto> getDeal(@PathVariable UUID id) {
        log.info("Получение сделки по ID: {}", id);
        return dealController.getDeal(id);
    }

    /**
     * Сохраняет сделку.
     *
     * @param dealDtoSave DTO сделки для сохранения
     * @return ResponseEntity с сохраненной сделкой
     */
    @PutMapping("/save")
    @PreAuthorize("hasAnyRole('DEAL_SUPERUSER', 'SUPERUSER')")
    public ResponseEntity<DealDtoSave> saveDeal(@RequestBody DealDtoSave dealDtoSave) {
        log.info("Сохранение сделки: {}", dealDtoSave);
        return dealController.saveDeal(dealDtoSave);
    }

    /**
     * Ищет сделки по фильтру.
     *
     * @param filter         фильтр поиска
     * @param pageable       параметры пагинации
     * @param authentication данные аутентификации
     * @return страница с найденными сделками
     */
    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('CREDIT_USER', 'OVERDRAFT_USER','DEAL_SUPERUSER','SUPERUSER')")
    public PageResponse<DealDto> searchDeals(@RequestBody DealSearchFilterDto filter, Pageable pageable,
        Authentication authentication) {
        log.info("Поиск сделки фильтр: {}, страница: {}", filter, pageable);
        Page<DealDto> page = dealService.searchDeals(filter, pageable, authentication);
        return new PageResponse<>(page);
    }

    /**
     * Экспортирует сделки в Excel.
     *
     * @param filter   фильтр поиска
     * @param pageable параметры пагинации
     * @return ResponseEntity с Excel файлом
     * @throws IOException если произошла ошибка при создании файла
     */
    @PostMapping("/search/export")
    @PreAuthorize("hasAnyRole('DEAL_SUPERUSER', 'SUPERUSER')")
    @Operation(summary = "Экспорт сделок в Excel", description = "Экспортирует список сделок в Excel файл")
    public ResponseEntity<ByteArrayResource> exportDeals(@RequestBody DealSearchFilterDto filter, Pageable pageable)
        throws IOException {
        log.info("Экспортирует сделки в Excel. Фильтр: {}, страница: {}", filter, pageable);
        return dealController.exportDeals(filter, pageable);
    }

}
