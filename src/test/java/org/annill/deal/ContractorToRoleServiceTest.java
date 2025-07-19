package org.annill.deal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;
import org.annill.deal.dto.ContractorToRoleDto;
import org.annill.deal.entity.ContractorRole;
import org.annill.deal.entity.ContractorToRole;
import org.annill.deal.entity.DealContractor;
import org.annill.deal.repository.ContractorToRoleRepository;
import org.annill.deal.repository.DealContractorRepository;
import org.annill.deal.service.ContractorToRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ContractorToRoleServiceTest {

    @Mock
    private ContractorToRoleRepository contractorToRoleRepository;

    @Mock
    private DealContractorRepository dealContractorRepository;

    @InjectMocks
    private ContractorToRoleService contractorToRoleService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
    }

    private String readJsonFile(String path) throws IOException {
        return Files.readString(new ClassPathResource(path).getFile().toPath());
    }

    @Test
    void save_ShouldCreateNewContractorToRole_WhenNotExists() throws Exception {
        String json = readJsonFile("data/valid_create_request.json");
        ContractorToRoleDto dto = objectMapper.readValue(json, ContractorToRoleDto.class);

        when(dealContractorRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(new DealContractor()));
        when(contractorToRoleRepository.findByContractorIdAndRoleId(any(UUID.class), anyString()))
            .thenReturn(Optional.empty());

        contractorToRoleService.save(dto);

        verify(contractorToRoleRepository).save(any(ContractorToRole.class));
        verify(dealContractorRepository).findById(dto.getDealContractorDto().getId());
    }

    @Test
    void save_ShouldActivateExisting_WhenExistsButInactive() throws Exception {
        String json = readJsonFile("data/valid_update_request.json");
        ContractorToRoleDto dto = objectMapper.readValue(json, ContractorToRoleDto.class);

        ContractorToRole existing = new ContractorToRole();
        ReflectionTestUtils.setField(existing, "isActive", false);

        when(contractorToRoleRepository.findByContractorIdAndRoleId(any(UUID.class), anyString()))
            .thenReturn(Optional.of(existing));

        contractorToRoleService.save(dto);

        assertTrue(existing.getIsActive());
        verify(contractorToRoleRepository).save(existing);
    }


    @Test
    void delete_ShouldDeactivateContractorToRole() throws Exception {
        String json = readJsonFile("data/valid_update_request.json");
        ContractorToRoleDto dto = objectMapper.readValue(json, ContractorToRoleDto.class);

        DealContractor dealContractor = new DealContractor().setId(dto.getDealContractorDto().getId());
        ContractorRole contractorRole = new ContractorRole().setId(dto.getContractorRoleDto().getId());
        ContractorToRole existing = new ContractorToRole()
            .setContractor(dealContractor)
            .setRole(contractorRole)
            .setIsActive(true);


        when(contractorToRoleRepository.findByContractorIdAndRoleId(any(UUID.class), anyString()))
            .thenReturn(Optional.of(existing));

        contractorToRoleService.delete(dto);

        verify(contractorToRoleRepository).save(existing);
    }
}