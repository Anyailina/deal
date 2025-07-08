package org.annill.deal.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.annill.deal.converter.DealConverter;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.entity.Deal;
import org.annill.deal.entity.DealStatus;
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
