package org.annill.deal.controller;


import java.util.UUID;
import lombok.AllArgsConstructor;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.filter.DealSearchFilterDto;
import org.annill.deal.service.DealService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@RestController
@RequestMapping("/deal")
@AllArgsConstructor
public class DealController {
    private DealService dealService;

    @PatchMapping("/change/status")
    public ResponseEntity<DealDto> changeDealStatus(@RequestParam UUID dealId,
        @RequestParam String newStatus) {
        return dealService.changeStatus(dealId, newStatus)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<DealDto> getDeal(@PathVariable UUID id) {
        return dealService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/save")
    public ResponseEntity<DealDtoSave> saveDeal(@RequestBody DealDtoSave dealDtoSave) {
        return ResponseEntity.ok(dealService.saveDeal(dealDtoSave));
    }

    @PostMapping("/search")
    public Page<DealDto> searchDeals(@RequestBody DealSearchFilterDto filter, Pageable pageable) {
        return dealService.searchDeals(filter, pageable);
    }

}
