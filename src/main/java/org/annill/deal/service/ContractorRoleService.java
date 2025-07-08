package org.annill.deal.service;


import lombok.AllArgsConstructor;
import org.annill.deal.entity.ContractorRole;
import org.annill.deal.repository.ContractorRoleRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractorRoleService {

    private final ContractorRoleRepository contractorRoleRepository;

    public ContractorRole findById(String id) {
        return contractorRoleRepository.findById(id).orElse(null);
    }

}
