package org.annill.deal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.annill.deal.converter.DealContractorConverter;
import org.annill.deal.dto.DealContractorDto;
import org.annill.deal.entity.DealContractor;
import org.annill.deal.repository.DealContractorRepository;
import org.annill.deal.service.DealContractorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealContractorServiceTest {

    @Mock
    private DealContractorRepository dealContractorRepository;

    @Mock
    private DealContractorConverter dealContractorConverter;

    @InjectMocks
    private DealContractorService dealContractorService;

    private DealContractorDto testDto;
    private DealContractor testEntity;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String dtoJson = new String(Files.readAllBytes(Paths.get("src/test/resources/data/deal_contractor_dto.json")));
        String entityJson = new String(Files.readAllBytes(Paths.get("src/test/resources/data/deal_contractor_entity.json")));

        testDto = objectMapper.readValue(dtoJson, DealContractorDto.class);
        testEntity = objectMapper.readValue(entityJson, DealContractor.class);
    }

    @Test
    void saveOrUpdate_WhenNewContractor_ShouldCreateNewEntity() {
        when(dealContractorRepository.findById(testDto.getId())).thenReturn(Optional.empty());
        when(dealContractorRepository.save(any(DealContractor.class))).thenReturn(testEntity);
        when(dealContractorConverter.toDto(any(DealContractor.class))).thenReturn(testDto);

        DealContractorDto result = dealContractorService.saveOrUpdate(testDto);

        assertNotNull(result);
        assertEquals(testDto.getId(), result.getId());
        assertEquals(testDto.getName(), result.getName());

        verify(dealContractorRepository, times(1)).findById(testDto.getId());
        verify(dealContractorRepository, times(1)).save(any(DealContractor.class));
    }

    @Test
    void saveOrUpdate_WhenExistingContractor_ShouldUpdateEntity() {
        when(dealContractorRepository.findById(testDto.getId())).thenReturn(Optional.of(testEntity));
        when(dealContractorRepository.save(any(DealContractor.class))).thenReturn(testEntity);
        when(dealContractorConverter.toDto(any(DealContractor.class))).thenReturn(testDto);

        testDto.setName("Updated Name");

        DealContractorDto result = dealContractorService.saveOrUpdate(testDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());

        verify(dealContractorRepository, times(1)).findById(testDto.getId());
        verify(dealContractorRepository, times(1)).save(any(DealContractor.class));
    }

    @Test
    void saveOrUpdate_ShouldSetModifyDate() {
        when(dealContractorRepository.findById(testDto.getId())).thenReturn(Optional.of(testEntity));
        when(dealContractorRepository.save(any(DealContractor.class))).thenAnswer(invocation -> {
            DealContractor saved = invocation.getArgument(0);
            assertNotNull(saved.getModifyDate());
            return saved;
        });
        when(dealContractorConverter.toDto(any(DealContractor.class))).thenReturn(testDto);

        dealContractorService.saveOrUpdate(testDto);

        verify(dealContractorRepository, times(1)).save(any(DealContractor.class));
    }

    @Test
    void delete_WhenContractorExists_ShouldDeactivate() {
        when(dealContractorRepository.findById(testDto.getId())).thenReturn(Optional.of(testEntity));
        when(dealContractorRepository.save(any(DealContractor.class))).thenAnswer(invocation -> {
            DealContractor saved = invocation.getArgument(0);
            assertFalse(saved.getIsActive());
            return saved;
        });

        dealContractorService.delete(testDto);

        verify(dealContractorRepository, times(1)).findById(testDto.getId());
        verify(dealContractorRepository, times(1)).save(any(DealContractor.class));
    }

    @Test
    void delete_WhenContractorNotExists_ShouldDoNothing() {
        when(dealContractorRepository.findById(testDto.getId())).thenReturn(Optional.empty());

        dealContractorService.delete(testDto);

        verify(dealContractorRepository, times(1)).findById(testDto.getId());
        verify(dealContractorRepository, never()).save(any(DealContractor.class));
    }
}