package org.annill.deal.repository;

import java.util.UUID;
import org.annill.deal.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, UUID> {

}
