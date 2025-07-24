package org.annill.deal.controller;

import org.annill.deal.dto.DealContractorDto;
import org.springframework.http.ResponseEntity;

public interface DealContractorApi {

    ResponseEntity<DealContractorDto> save(DealContractorDto contractor);

    ResponseEntity<Void> delete(DealContractorDto contractor);

}
