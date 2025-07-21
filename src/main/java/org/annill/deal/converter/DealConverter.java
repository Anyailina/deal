package org.annill.deal.converter;

import java.util.List;
import lombok.AllArgsConstructor;
import org.annill.deal.dto.DealContractorDto;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.dto.DealSumDto;
import org.annill.deal.entity.Deal;
import org.annill.deal.entity.DealContractor;
import org.annill.deal.entity.DealSum;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DealConverter {

    private DealTypeConverter dealTypeConverter;
    private DealStatusConverter dealStatusConverter;
    private DealSumConverter dealSumConverter;
    private DealContractorConverter dealContractorConverter;

    public DealDto toDto(Deal deal) {

        List<DealSumDto> dealSumDtoList = deal
            .getDealSumList().stream()
            .map(dealSumConverter::toDto).toList();

        List<DealContractorDto> dealContractorDtoList = deal
            .getDealContractorList().stream()
            .map(dealContractorConverter::toDto)
            .toList();

        return new DealDto()
            .setId(deal.getId())
            .setDescription(deal.getDescription())
            .setAgreementNumber(deal.getAgreementNumber())
            .setAgreementDate(deal.getAgreementDate())
            .setAgreementStartDt(deal.getAgreementStartDt())
            .setAvailabilityDate(deal.getAvailabilityDate())
            .setCloseDt(deal.getCloseDt())
            .setDealStatusDto(dealStatusConverter.toDto(deal.getStatus()))
            .setDealTypeDto(dealTypeConverter.toDto(deal.getType()))
            .setDealSumDtoList(dealSumDtoList)
            .setDealContractorDtoList(dealContractorDtoList);
    }

    public Deal toEntity(DealDto dealDto) {

        List<DealSum> dealSumList = dealDto
            .getDealSumDtoList().stream()
            .map(dealSumConverter::toEntity).toList();

        List<DealContractor> dealContractorList = dealDto
            .getDealContractorDtoList().stream()
            .map(dealContractorConverter::toEntity)
            .toList();

        return new Deal()
            .setId(dealDto.getId())
            .setDescription(dealDto.getDescription())
            .setAgreementNumber(dealDto.getAgreementNumber())
            .setAgreementDate(dealDto.getAgreementDate())
            .setAgreementStartDt(dealDto.getAgreementStartDt())
            .setAvailabilityDate(dealDto.getAvailabilityDate())
            .setCloseDt(dealDto.getCloseDt())
            .setStatus(dealStatusConverter.toEntity(dealDto.getDealStatusDto()))
            .setType(dealTypeConverter.toEntity(dealDto.getDealTypeDto()))
            .setDealSumList(dealSumList)
            .setDealContractorList(dealContractorList);
    }

    public DealDtoSave toSaveDto(Deal deal) {

        List<DealSumDto> dealSumDtoList = deal
            .getDealSumList().stream()
            .map(dealSumConverter::toDto).toList();

        List<DealContractorDto> dealContractorDtoList = deal
            .getDealContractorList().stream()
            .map(dealContractorConverter::toDto)
            .toList();

        return new DealDtoSave()
            .setId(deal.getId())
            .setDescription(deal.getDescription())
            .setAgreementNumber(deal.getAgreementNumber())
            .setAgreementDate(deal.getAgreementDate())
            .setAgreementStartDt(deal.getAgreementStartDt())
            .setAvailabilityDate(deal.getAvailabilityDate())
            .setCloseDt(deal.getCloseDt())
            .setDealTypeDto(dealTypeConverter.toDto(deal.getType()))
            .setDealSumDtoList(dealSumDtoList)
            .setDealContractorDtoList(dealContractorDtoList);
    }

    public Deal toEntity(DealDtoSave dealDto) {

        List<DealSum> dealSumList = dealDto
            .getDealSumDtoList().stream()
            .map(dealSumConverter::toEntity).toList();

        List<DealContractor> dealContractorList = dealDto
            .getDealContractorDtoList().stream()
            .map(dealContractorConverter::toEntity)
            .toList();

        return new Deal()
            .setId(dealDto.getId())
            .setDescription(dealDto.getDescription())
            .setAgreementNumber(dealDto.getAgreementNumber())
            .setAgreementDate(dealDto.getAgreementDate())
            .setAgreementStartDt(dealDto.getAgreementStartDt())
            .setAvailabilityDate(dealDto.getAvailabilityDate())
            .setCloseDt(dealDto.getCloseDt())
            .setType(dealTypeConverter.toEntity(dealDto.getDealTypeDto()))
            .setDealSumList(dealSumList)
            .setDealContractorList(dealContractorList);
    }

}
