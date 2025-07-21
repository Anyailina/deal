package org.annill.deal.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.annill.deal.entity.DealStatus;
import org.annill.deal.repository.DealStatusRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DealStatusService {

    private DealStatusRepository dealStatusRepository;

    public Optional<DealStatus> findByName(String name) {
        return dealStatusRepository.findFirstByName(name);

    }

}
