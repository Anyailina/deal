package org.annill.deal.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.annill.deal.converter.DealConverter;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.entity.Deal;
import org.annill.deal.repository.DealRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DealService {

    private DealStatusService dealStatusService;
    private DealRepository dealRepository;
    private DealConverter dealConverter;

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


    public Optional<DealDto> getById(UUID id) {
        return dealRepository.findById(id)
            .filter(Deal::isActive)
            .map(dealConverter::toDto);
    }




}
