package org.annill.deal.controller.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.annill.deal.controller.DealContractorApi;
import org.annill.deal.dto.DealContractorDto;
import org.annill.deal.service.DealContractorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления контрагентами сделок через UI. Предоставляет endpoints для сохранения и удаления
 * контрагентов.
 */
@RestController
@RequestMapping("ui/deal-contractor")
@RequiredArgsConstructor
@Slf4j
public class UiDealContractorController implements DealContractorApi {

    private final DealContractorService dealContractorService;

    /**
     * Сохраняет контрагента сделки.
     *
     * @param contractor DTO контрагента для сохранения
     * @return ResponseEntity с сохраненным контрагентом
     */
    @PutMapping("/save")
    @PreAuthorize("hasAnyRole('DEAL_SUPERUSER', 'SUPERUSER')")
    public ResponseEntity<DealContractorDto> save(@RequestBody DealContractorDto contractor) {
        log.info("Сохранение контрагента {}", contractor);
        return ResponseEntity.ok(dealContractorService.saveOrUpdate(contractor));
    }

    /**
     * Удаляет контрагента сделки.
     *
     * @param contractor DTO контрагента для удаления
     * @return ResponseEntity без содержимого
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('DEAL_SUPERUSER', 'SUPERUSER')")
    public ResponseEntity<Void> delete(@RequestBody DealContractorDto contractor) {
        log.info("Удаление контрагента: {}", contractor);
        dealContractorService.delete(contractor);
        return ResponseEntity.ok().build();
    }

}
