package org.annill.deal.converter;

import lombok.AllArgsConstructor;
import org.annill.deal.dto.DealSumDto;
import org.annill.deal.entity.Currency;
import org.annill.deal.entity.DealSum;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DealSumConverter {

    public DealSumDto toDto(DealSum dealSum) {
        return new DealSumDto()
            .setSum(dealSum.getSum())
            .setCurrency(dealSum.getCurrency().getId());

    }

    public DealSum toEntity(DealSumDto dealSumDto) {
        return new DealSum()
            .setSum(dealSumDto.getSum())
            .setCurrency(new Currency().setId(dealSumDto.getCurrency()));

    }

}
