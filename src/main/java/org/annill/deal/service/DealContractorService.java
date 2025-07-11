package org.annill.deal.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.annill.deal.converter.DealContractorConverter;
import org.annill.deal.dto.DealContractorDto;
import org.annill.deal.entity.DealContractor;
import org.annill.deal.repository.DealContractorRepository;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления контрагентами сделок.
 * Позволяет сохранять, обновлять и деактивировать контрагентов сделок.
 */
@Service
@AllArgsConstructor
public class DealContractorService {

    private DealContractorRepository dealContractorRepository;
    private DealContractorConverter dealContractorConverter;

    /**
     * Сохраняет или обновляет контрагента сделки.
     */
    public DealContractorDto saveOrUpdate(DealContractorDto dealContractorDto) {
        DealContractor dealContractor = dealContractorRepository
            .findById(dealContractorDto.getId())
            .orElse(new DealContractor().setId(dealContractorDto.getId()));

        dealContractor
            .setContractorId(dealContractorDto.getContractorId())
            .setName(dealContractorDto.getName())
            .setMain(dealContractorDto.getMain())
            .setModifyDate(LocalDateTime.now());

        return dealContractorConverter
            .toDto(dealContractorRepository.save(dealContractor));
    }

    /**
     * Деактивирует контрагента сделки.
     */
    public void delete(DealContractorDto dealContractorDto) {
        Optional<DealContractor> optionalDealContractor = dealContractorRepository
            .findById(dealContractorDto.getId());
        optionalDealContractor.ifPresent(
            dealContractor -> dealContractorRepository.save(dealContractor.setIsActive(false)));
    }

}
