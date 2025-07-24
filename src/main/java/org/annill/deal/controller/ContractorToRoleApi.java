package org.annill.deal.controller;

import org.annill.deal.dto.ContractorToRoleDto;
import org.springframework.http.ResponseEntity;

public interface ContractorToRoleApi {

    ResponseEntity<?> save(ContractorToRoleDto contractorToRoleDto);

    ResponseEntity<?> delete(ContractorToRoleDto contractorToRoleDto);

}
