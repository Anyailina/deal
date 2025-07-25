package org.annill.deal;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.InputStream;
import org.annill.deal.controller.DealContractorController;
import org.annill.deal.dto.DealContractorDto;
import org.annill.deal.security.AuthTokenFilter;
import org.annill.deal.security.JwtUtils;
import org.annill.deal.service.DealContractorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(DealContractorController.class)
@AutoConfigureMockMvc(addFilters = false)
class DealContractorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DealContractorService dealContractorService;

    @MockitoBean
    private AuthTokenFilter authTokenFilter;


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
    void testSaveContractor() throws Exception {
        when(dealContractorService.saveOrUpdate(testContractorDto)).thenReturn(testContractorDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/deal-contractor/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testContractorDto)))
            .andExpect(status().isOk())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        DealContractorDto returnedDto = objectMapper.readValue(responseBody, DealContractorDto.class);

        Assertions.assertEquals(testContractorDto, returnedDto);
    }


    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doThrow(new EmptyResultDataAccessException(1))
            .when(dealContractorService)
            .delete(testContractorDto);
        mockMvc.perform(delete("/deal-contractor/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testContractorDto)))
            .andExpect(status().isNotFound())
            .andReturn();
        ;

    }
}