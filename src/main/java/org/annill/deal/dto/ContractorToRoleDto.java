package org.annill.deal.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ContractorToRoleDto {

    private DealContractorDto dealContractorDto;

    private ContractorRoleDto contractorRoleDto;

}
