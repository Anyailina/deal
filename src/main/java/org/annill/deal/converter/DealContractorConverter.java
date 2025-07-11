package org.annill.deal.converter;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.annill.deal.dto.ContractorRoleDto;
import org.annill.deal.dto.DealContractorDto;
import org.annill.deal.entity.ContractorRole;
import org.annill.deal.entity.ContractorToRole;
import org.annill.deal.entity.DealContractor;
import org.annill.deal.service.ContractorRoleService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DealContractorConverter {

    private ContractorRoleService contractorRoleService;

    public DealContractorDto toDto(DealContractor dealContractor) {
        List<ContractorRoleDto> contractorRoleList = dealContractor.getContractorToRoleList() != null
            ? dealContractor.getContractorToRoleList().stream()
            .map(ContractorToRole::getRole)
            .filter(contractorRole -> contractorRole.getIsActive() != null && contractorRole.getIsActive())
            .map(role -> new ContractorRoleDto()
                .setId(role.getId())
                .setName(role.getName())
                .setCategory(role.getCategory()))
            .collect(Collectors.toList())
            : null;

        return new DealContractorDto()
            .setId(dealContractor.getId())
            .setContractorId(dealContractor.getContractorId())
            .setName(dealContractor.getName())
            .setMain(dealContractor.getMain())
            .setContractorRoleDtoList(contractorRoleList);

    }

    public DealContractor toEntity(DealContractorDto dealContractorDto) {
        DealContractor entity = new DealContractor()
            .setId(dealContractorDto.getId())
            .setContractorId(dealContractorDto.getContractorId())
            .setName(dealContractorDto.getName())
            .setMain(dealContractorDto.getMain());

        if (dealContractorDto.getContractorRoleDtoList() != null) {
            List<ContractorToRole> contractorToRoleList = dealContractorDto.getContractorRoleDtoList().stream()
                .map(roleDto -> {
                    ContractorRole contractorRole = contractorRoleService.findById(roleDto.getId());

                    ContractorToRole contractorToRole = new ContractorToRole();
                    contractorToRole.setRole(contractorRole);
                    contractorToRole.setContractor(entity);
                    contractorToRole.setIsActive(true);
                    return contractorToRole;
                })
                .collect(Collectors.toList());

            entity.setContractorToRoleList(contractorToRoleList);
        }

        return entity;

    }

}
