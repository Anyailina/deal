package org.annill.deal.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DealSumDto {

    private BigDecimal sum;
    private String currency;

}
