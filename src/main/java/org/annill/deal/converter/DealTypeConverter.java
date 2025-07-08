package org.annill.deal.converter;

import org.annill.deal.dto.DealTypeDto;
import org.annill.deal.entity.DealType;
import org.springframework.stereotype.Component;

@Component
public class DealTypeConverter {

    public DealTypeDto toDto(DealType dealType) {
        return new DealTypeDto()
            .setId(dealType.getId())
            .setName(dealType.getName());

    }

    public DealType toEntity(DealTypeDto dealTypeDto) {
        return new DealType()
            .setId(dealTypeDto.getId())
            .setName(dealTypeDto.getName());

    }
}
