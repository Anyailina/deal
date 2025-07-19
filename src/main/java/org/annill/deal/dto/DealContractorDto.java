package org.annill.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema
public class DealContractorDto {

    private UUID id;
    private String contractorId;
    private String name;
    private Boolean main;
    private List<ContractorRoleDto> contractorRoleDtoList;

}
