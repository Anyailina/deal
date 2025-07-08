package org.annill.deal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DealDtoSave {

    private UUID id;

    private String description;

    private String agreementNumber;

    private LocalDate agreementDate;

    private LocalDateTime agreementStartDt;

    private LocalDate availabilityDate;

    private LocalDateTime closeDt;

    private DealTypeDto dealTypeDto;


    private List<DealSumDto> dealSumDtoList;

    private List<DealContractorDto> dealContractorDtoList;
}
