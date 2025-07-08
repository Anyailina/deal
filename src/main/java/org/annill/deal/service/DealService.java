package org.annill.deal.service;

import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.annill.deal.converter.DealConverter;
import org.annill.deal.dto.DealDto;
import org.annill.deal.entity.Deal;
import org.annill.deal.repository.DealRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DealService {


    private DealRepository dealRepository;
    private DealConverter dealConverter;


    public Optional<DealDto> getById(UUID id) {
        return dealRepository.findById(id)
            .filter(Deal::isActive)
            .map(dealConverter::toDto);
    }


}
