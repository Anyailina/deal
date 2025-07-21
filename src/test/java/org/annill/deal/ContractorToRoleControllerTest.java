package org.annill.deal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.annill.deal.controller.ContractorToRoleController;
import org.annill.deal.dto.ContractorToRoleDto;
import org.annill.deal.service.ContractorToRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ContractorToRoleController.class)
class ContractorToRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private ContractorToRoleService contractorToRoleService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private ContractorToRoleDto testDto;

    @BeforeEach
    void setUp() throws IOException {
        String jsonSearch = Files.readString(Paths.get("src/test/resources/data/contractor-to-role.json"));
        testDto = objectMapper.readValue(jsonSearch, ContractorToRoleDto.class);
    }

    @Test
    void save_ShouldCallServiceMethod() throws Exception {

        mockMvc.perform(put("/contractor-to-role/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDto)))
            .andExpect(status().isOk());

        verify(contractorToRoleService).save(any(ContractorToRoleDto.class));
    }

    @Test
    void delete_ShouldCallServiceMethod() throws Exception {
        doNothing().when(contractorToRoleService).delete(any(ContractorToRoleDto.class));

        mockMvc.perform(delete("/contractor-to-role/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDto)))
            .andExpect(status().isOk());

        verify(contractorToRoleService).delete(any(ContractorToRoleDto.class));
    }

}
