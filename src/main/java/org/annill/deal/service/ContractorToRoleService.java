package org.annill.deal.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.annill.deal.dto.ContractorRoleDto;
import org.annill.deal.dto.ContractorToRoleDto;
import org.annill.deal.entity.ContractorRole;
import org.annill.deal.entity.ContractorToRole;
import org.annill.deal.entity.DealContractor;
import org.annill.deal.repository.ContractorToRoleRepository;
import org.annill.deal.repository.DealContractorRepository;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления связью между контрагентами сделок и их ролями.
 * <p>
 * Позволяет добавлять и удалять роли у контрагентов сделок.
 * </p>
 */
@Service
@AllArgsConstructor
public class ContractorToRoleService {

    private ContractorToRoleRepository contractorToRoleRepository;
    private DealContractorRepository dealContractorRepository;

    /**
     * Сохраняет связь между контрагентом сделки и ролью.
     * <p>
     * Если связь уже существует, активирует ее.
     * Если нет, создает новую связь с активным статусом.
     * </p>
     *
     */
    public void save(ContractorToRoleDto contractorToRoleDto) {
        UUID contractorId = contractorToRoleDto.getDealContractorDto().getId();
        String roleId = contractorToRoleDto.getContractorRoleDto().getId();
        ContractorRoleDto contractorRole = contractorToRoleDto.getContractorRoleDto();

        Optional<ContractorToRole> contractorToRoleOpt = contractorToRoleRepository
            .findByContractorIdAndRoleId(contractorId, roleId);

        if (contractorToRoleOpt.isPresent()) {
            contractorToRoleRepository.save(contractorToRoleOpt.get().setIsActive(true));
        } else {
            Optional<DealContractor> dealContractor =
                dealContractorRepository.findById(contractorId);

            if (dealContractor.isPresent()) {
                ContractorRole newContractorRole = new ContractorRole()
                    .setId(contractorRole.getId())
                    .setName(contractorRole.getName())
                    .setCategory(contractorRole.getCategory())
                    .setIsActive(true);

                contractorToRoleRepository.save(new ContractorToRole()
                    .setContractor(dealContractor.get())
                    .setRole(newContractorRole)
                    .setIsActive(true));

            } else {
                throw new EntityNotFoundException("DealContractor with id " + contractorId + " not found");
            }
        }
    }

    /**
     * Удаляет связь между контрагентом сделки и ролью.
     */
    public void delete(ContractorToRoleDto contractorToRoleDto) {
        UUID contractorId = contractorToRoleDto.getDealContractorDto().getId();
        String roleId = contractorToRoleDto.getContractorRoleDto().getId();

        Optional<ContractorToRole> contractorToRoleOpt = contractorToRoleRepository
            .findByContractorIdAndRoleId(contractorId, roleId);

        if (contractorToRoleOpt.isPresent()) {
            ContractorRole updatedRole = contractorToRoleOpt.get().getRole().setIsActive(false);
            ContractorToRole updatedContractorToRole = contractorToRoleOpt.get().setRole(updatedRole);
            contractorToRoleRepository.save(updatedContractorToRole);
        }
    }

}
