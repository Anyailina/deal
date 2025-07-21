package org.annill.deal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import org.annill.deal.controller.DealController;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.filter.DealSearchFilterDto;
import org.annill.deal.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DealControllerTest {

    @Mock
    private DealService dealService;

    @InjectMocks
    private DealController dealController;

    private ObjectMapper objectMapper;

    private DealDto testDealDto;
    private DealDtoSave testDealDtoSave;
    private DealSearchFilterDto testFilterDto;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        String jsonDealDto = Files.readString(Paths.get("src/test/resources/data/deal-dto.json"));
         String jsonDealDtoSave = Files.readString(Paths.get("src/test/resources/data/deal-dto-save.json"));
        String jsonDealFilterDto = Files.readString(Paths.get("src/test/resources/data/deal-search-filter.json"));
        testDealDto = objectMapper.readValue(jsonDealDto, DealDto.class);
        testDealDtoSave = objectMapper.readValue(jsonDealDtoSave, DealDtoSave.class);
        testFilterDto = objectMapper.readValue(jsonDealFilterDto, DealSearchFilterDto.class);
    }


    @Test
    void changeDealStatus_WhenDealExists_ShouldReturnOk() {

        String newStatus = "ACTIVE";

        when(dealService.changeStatus(testDealDto.getId(), newStatus))
            .thenReturn(Optional.of(testDealDto));

        ResponseEntity<DealDto> response = dealController.changeDealStatus(testDealDto.getId(), newStatus);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testDealDto, response.getBody());
        verify(dealService).changeStatus(testDealDto.getId(), newStatus);
    }

    @Test
    void getDeal_WhenDealExists_ShouldReturnOk() {

        when(dealService.getById(testDealDto.getId())).thenReturn(Optional.of(testDealDto));

        ResponseEntity<DealDto> response = dealController.getDeal(testDealDto.getId());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testDealDto, response.getBody());
        verify(dealService).getById(testDealDto.getId());
    }

    @Test
    void saveDeal_ShouldReturnSavedDeal() {
        when(dealService.saveDeal(testDealDtoSave)).thenReturn(testDealDtoSave);

        ResponseEntity<DealDtoSave> response = dealController.saveDeal(testDealDtoSave);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testDealDtoSave, response.getBody());
        verify(dealService).saveDeal(testDealDtoSave);
    }

    @Test
    void searchDeals_ShouldReturnPageResponse() {
        Page<DealDto> page = new PageImpl<>(Collections.singletonList(testDealDto));
        Pageable pageable = Pageable.unpaged();

        when(dealService.searchDeals(testFilterDto, pageable)).thenReturn(page);

        PageResponse<DealDto> response = dealController.searchDeals(testFilterDto, pageable);

        assertEquals(1, response.pageSize());
        assertEquals(testDealDto, response.content().get(0));
        verify(dealService).searchDeals(testFilterDto, pageable);
    }
}
