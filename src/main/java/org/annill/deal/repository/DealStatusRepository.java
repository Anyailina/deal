package org.annill.deal.repository;

import java.util.Optional;
import org.annill.deal.entity.DealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealStatusRepository extends JpaRepository<DealStatus, String> {

    Optional<DealStatus> findFirstByName(String name);

}
