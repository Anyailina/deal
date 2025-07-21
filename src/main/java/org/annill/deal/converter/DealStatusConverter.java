package org.annill.deal.converter;

import org.annill.deal.dto.DealStatusDto;
import org.annill.deal.entity.DealStatus;
import org.springframework.stereotype.Component;

@Component
public class DealStatusConverter {

    public DealStatusDto toDto(DealStatus dealStatus) {
        return new DealStatusDto()
            .setId(dealStatus.getId())
            .setName(dealStatus.getName());

    }

    public DealStatus toEntity(DealStatusDto dealStatusDto) {
        return new DealStatus()
            .setId(dealStatusDto.getId())
            .setName(dealStatusDto.getName());

    }

}
