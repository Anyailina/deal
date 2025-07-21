package org.annill.deal.repository;

import java.util.Optional;
import java.util.UUID;
import org.annill.deal.entity.ContractorToRole;
import org.annill.deal.entity.ContractorToRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorToRoleRepository extends JpaRepository<ContractorToRole, ContractorToRoleId> {

    Optional<ContractorToRole> findByContractorIdAndRoleId(UUID contractorId, String roleId);

}
