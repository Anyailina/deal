package org.annill.deal.repository;

import java.util.UUID;
import org.annill.deal.entity.DealContractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealContractorRepository extends JpaRepository<DealContractor, UUID> {

}
