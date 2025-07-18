package org.annill.deal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.annill.deal.controller.DealContractorController;
import org.annill.deal.dto.DealContractorDto;
import org.annill.deal.service.DealContractorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealContractorControllerTest {

    @Mock
    private DealContractorService dealContractorService;

    @InjectMocks
    private DealContractorController dealContractorController;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    private DealContractorDto testContractorDto;

    @BeforeEach
    void setUp() throws Exception {
        try (InputStream is = new ClassPathResource("data/deal_contractor_dto.json").getInputStream()) {
            this.testContractorDto = objectMapper.readValue(is, DealContractorDto.class);
        }
    }

    @Test
    void save_ShouldReturnSavedContractor() {
        when(dealContractorService.saveOrUpdate(any(DealContractorDto.class)))
            .thenReturn(testContractorDto);

        ResponseEntity<DealContractorDto> response =
            dealContractorController.save(testContractorDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testContractorDto, response.getBody());
        verify(dealContractorService).saveOrUpdate(testContractorDto);
    }

    @Test
    void delete_ShouldCallServiceMethod() {
        doNothing().when(dealContractorService).delete(any(DealContractorDto.class));

        dealContractorController.delete(testContractorDto);

        verify(dealContractorService).delete(testContractorDto);
    }

    @Test
    void delete_ShouldReturnNoContent_WhenSuccess() {

        doNothing().when(dealContractorService).delete(any(DealContractorDto.class));

        dealContractorController.delete(testContractorDto);

        verify(dealContractorService).delete(testContractorDto);
    }

}