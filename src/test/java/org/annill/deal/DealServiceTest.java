package org.annill.deal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.annill.deal.converter.DealConverter;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.entity.Deal;
import org.annill.deal.entity.DealStatus;
import org.annill.deal.filter.DealSearchFilterDto;
import org.annill.deal.repository.DealRepository;
import org.annill.deal.service.DealService;
import org.annill.deal.service.DealStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    private DealStatusService dealStatusService;

    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealConverter dealConverter;

    @InjectMocks
    private DealService dealService;

    @Test
    void changeStatus_WhenDealAndStatusExist_ShouldReturnUpdatedDealDto() {
        UUID dealId = UUID.randomUUID();
        String newStatus = "ACTIVE";
        Deal deal = new Deal();
        DealStatus status = new DealStatus();
        Deal savedDeal = new Deal();
        DealDto dealDto = new DealDto();

        when(dealRepository.findById(dealId)).thenReturn(Optional.of(deal));
        when(dealStatusService.findByName(newStatus)).thenReturn(Optional.of(status));
        when(dealRepository.save(deal)).thenReturn(savedDeal);
        when(dealConverter.toDto(savedDeal)).thenReturn(dealDto);

        Optional<DealDto> result = dealService.changeStatus(dealId, newStatus);

        assertTrue(result.isPresent());
        assertEquals(dealDto, result.get());
        verify(dealRepository).findById(dealId);
        verify(dealStatusService).findByName(newStatus);
        verify(dealRepository).save(deal);
        verify(dealConverter).toDto(savedDeal);
    }

    @Test
    void changeStatus_WhenDealNotFound_ShouldReturnEmptyOptional() {
        UUID dealId = UUID.randomUUID();
        String newStatus = "ACTIVE";

        when(dealRepository.findById(dealId)).thenReturn(Optional.empty());

        Optional<DealDto> result = dealService.changeStatus(dealId, newStatus);

        assertFalse(result.isPresent());
        verify(dealRepository).findById(dealId);
        verifyNoInteractions(dealStatusService);
        verifyNoMoreInteractions(dealRepository);
        verifyNoInteractions(dealConverter);
    }

    @Test
    void getById_WhenDealExistsAndActive_ShouldReturnDealDto() {
        UUID id = UUID.randomUUID();
        Deal deal = new Deal();
        deal.setActive(true);
        DealDto dealDto = new DealDto();

        when(dealRepository.findById(id)).thenReturn(Optional.of(deal));
        when(dealConverter.toDto(deal)).thenReturn(dealDto);

        Optional<DealDto> result = dealService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(dealDto, result.get());
        verify(dealRepository).findById(id);
        verify(dealConverter).toDto(deal);
    }

    @Test
    void getById_WhenDealNotActive_ShouldReturnEmptyOptional() {
        UUID id = UUID.randomUUID();
        Deal deal = new Deal();
        deal.setActive(false);

        when(dealRepository.findById(id)).thenReturn(Optional.of(deal));

        Optional<DealDto> result = dealService.getById(id);

        assertFalse(result.isPresent());
        verify(dealRepository).findById(id);
        verifyNoInteractions(dealConverter);
    }

    @Test
    void searchDeals_ShouldReturnPageOfDealDtos() {
        DealSearchFilterDto filter = new DealSearchFilterDto();
        Pageable pageable = Pageable.unpaged();
        Page<Deal> dealPage = new PageImpl<>(List.of(new Deal()));

        when(dealRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(dealPage);
        when(dealConverter.toDto(any(Deal.class))).thenReturn(new DealDto());

        Page<DealDto> result = dealService.searchDeals(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(dealRepository).findAll(any(Specification.class), eq(pageable));
        verify(dealConverter).toDto(any(Deal.class));
    }

    @Test
    void saveDeal_WhenDraftStatusExists_ShouldReturnSavedDealDto() {
        DealDtoSave dtoSave = new DealDtoSave();
        DealStatus draftStatus = new DealStatus();
        Deal newDeal = new Deal();
        Deal savedDeal = new Deal();
        DealDtoSave expectedDto = new DealDtoSave();

        when(dealStatusService.findByName("DRAFT")).thenReturn(Optional.of(draftStatus));
        when(dealConverter.toEntity(dtoSave)).thenReturn(newDeal);
        when(dealRepository.save(newDeal)).thenReturn(savedDeal);
        when(dealConverter.toSaveDto(savedDeal)).thenReturn(expectedDto);

        DealDtoSave result = dealService.saveDeal(dtoSave);

        assertEquals(expectedDto, result);
        verify(dealStatusService).findByName("DRAFT");
        verify(dealConverter).toEntity(dtoSave);
        verify(dealRepository).save(newDeal);
        verify(dealConverter).toSaveDto(savedDeal);
    }

    @Test
    void saveDeal_WhenDraftStatusNotExists_ShouldThrowException() {
        DealDtoSave dtoSave = new DealDtoSave();
        when(dealStatusService.findByName("DRAFT")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> dealService.saveDeal(dtoSave));
        verify(dealStatusService).findByName("DRAFT");
        verifyNoInteractions(dealConverter);
        verifyNoInteractions(dealRepository);
    }
}