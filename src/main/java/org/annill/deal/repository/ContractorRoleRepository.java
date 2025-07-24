package org.annill.deal.repository;

import org.annill.deal.entity.ContractorRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorRoleRepository extends JpaRepository<ContractorRole, String> {

}
