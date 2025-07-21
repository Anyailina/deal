package org.annill.deal.filter;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class DealSearchFilterDto {

    private String dealId;
    private String description;
    private String agreementNumber;
    private LocalDate agreementDateFrom;
    private LocalDate agreementDateTo;
    private LocalDate availabilityDateFrom;
    private LocalDate availabilityDateTo;
    private List<String> type;
    private List<String> status;
    private LocalDate closeDtFrom;
    private LocalDate closeDtTo;
    private String borrowerSearch;
    private String warranitySearch;
    private SumFilter sum;

}
