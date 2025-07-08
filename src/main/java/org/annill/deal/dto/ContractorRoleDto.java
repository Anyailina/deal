package org.annill.deal.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ContractorRoleDto {

    private String id;
    private String name;
    private String category;


}
