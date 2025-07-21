package org.annill.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.annill.deal.page.PageResponse;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.filter.DealSearchFilterDto;
import org.annill.deal.service.DealService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * Контроллер для управления сделками.
 * Предоставляет эндпоинты для получения, сохранения, изменения статуса, поиска и экспорта сделок.
 */
@Slf4j
@RestController
@RequestMapping("/deal")
@AllArgsConstructor
public class DealController {

    private DealService dealService;

    /**
     * Изменение статуса сделки по ID.
     *
     * @param dealId   ID сделки.
     * @param newStatus Новый статус сделки.
     */
    @PatchMapping("/change/status")
    @Operation(summary = "Изменить статус сделки", description = "Изменяет статус сделки по её ID")
    public ResponseEntity<DealDto> changeDealStatus(@RequestParam UUID dealId,
        @RequestParam String newStatus) {
        log.info("Изменение статуса сделки");
        return dealService.changeStatus(dealId, newStatus)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Получение сделки по ID.
     *
     * @param id ID сделки.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить сделку по ID", description = "Возвращает сделку по её идентификатору")
    public ResponseEntity<DealDto> getDeal(@PathVariable UUID id) {
        log.info("Получение сделки");
        return dealService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Сохранение новой сделки.
     *
     * @param dealDtoSave DTO для сохранения сделки.
     */
    @PutMapping("/save")
    @Operation(summary = "Сохранить сделку", description = "Сохраняет новую сделку в системе")
    public ResponseEntity<DealDtoSave> saveDeal(@RequestBody DealDtoSave dealDtoSave) {
        log.info("Сохранение сделки");
        return ResponseEntity.ok(dealService.saveDeal(dealDtoSave));
    }

    /**
     * Поиск сделок по фильтру с пагинацией.
     *
     * @param filter   DTO с фильтром поиска.
     * @param pageable Параметры пагинации.
     */
    @PostMapping("/search")
    @Operation(summary = "Поиск сделок", description = "Ищет сделки по фильтру с пагинацией")
    public PageResponse<DealDto> searchDeals(@RequestBody DealSearchFilterDto filter, Pageable pageable) {
        log.info("Поиск сделки");
        Page<DealDto> page = dealService.searchDeals(filter, pageable);
        return new PageResponse<>(page);
    }

    /**
     * Экспорт сделок по фильтру в Excel.
     *
     * @param filter   DTO с фильтром поиска.
     * @param pageable Параметры пагинации.
     */
    @PostMapping("/search/export")
    @Operation(summary = "Экспорт сделок в Excel", description = "Экспортирует список сделок в Excel файл")
    public ResponseEntity<ByteArrayResource> exportDeals(@RequestBody DealSearchFilterDto filter, Pageable pageable)
        throws IOException {
        log.info("Экспорт сделки");
        byte[] excelBytes = dealService.exportDeals(filter, pageable);

        ByteArrayResource resource = new ByteArrayResource(excelBytes);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deals.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .contentLength(excelBytes.length)
            .body(resource);
    }

}
