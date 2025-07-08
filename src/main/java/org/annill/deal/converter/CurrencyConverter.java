package org.annill.deal.converter;

import org.annill.deal.dto.CurrencyDto;
import org.annill.deal.entity.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConverter {

    public CurrencyDto toDto(Currency currency) {
        return new CurrencyDto()
            .setName(currency.getName());

    }

    public Currency toEntity(CurrencyDto currencyDto) {
        return new Currency()
            .setName(currencyDto.getName());

    }

}
