package org.annill.deal.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DealStatusDto {

    private String id;
    private String name;

}
