package org.annill.deal.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DealContractorDto {

    private UUID id;
    private String contractorId;
    private String name;
    private Boolean main;
    private List<ContractorRoleDto> contractorRoleDtoList;

}
