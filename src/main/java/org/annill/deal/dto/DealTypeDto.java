package org.annill.deal.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DealTypeDto {

    private String id;
    private String name;
}
