package org.annill.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.annill.deal.dto.DealContractorDto;
import org.annill.deal.service.DealContractorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/deal-contractor")
@AllArgsConstructor
public class DealContractorController {

    private DealContractorService dealContractorService;

    @PutMapping("/save")
    @Operation(summary = "Сохранение контрагента")
    public ResponseEntity<DealContractorDto> save(@RequestBody DealContractorDto contractor) {
        log.info("Сохранение контрагента");
        DealContractorDto savedContractor = dealContractorService.saveOrUpdate(contractor);
        return ResponseEntity.ok(savedContractor);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Удаление контрагента")
    public void delete(@RequestBody DealContractorDto contractor) {
        log.info("Удаление контрагента");
        dealContractorService.delete(contractor);
    }

}
