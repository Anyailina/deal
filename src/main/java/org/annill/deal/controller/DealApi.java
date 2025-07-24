package org.annill.deal.controller;

import java.io.IOException;
import java.util.UUID;
import org.annill.deal.dto.DealDto;
import org.annill.deal.dto.DealDtoSave;
import org.annill.deal.filter.DealSearchFilterDto;
import org.annill.deal.page.PageResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DealApi {

    ResponseEntity<DealDto> changeDealStatus(UUID dealId, String newStatus);

    ResponseEntity<DealDto> getDeal(UUID id);

    ResponseEntity<DealDtoSave> saveDeal(DealDtoSave dealDtoSave);

    PageResponse<DealDto> searchDeals(DealSearchFilterDto filter, Pageable pageable);

    ResponseEntity<ByteArrayResource> exportDeals(DealSearchFilterDto filter, Pageable pageable) throws IOException;

}
