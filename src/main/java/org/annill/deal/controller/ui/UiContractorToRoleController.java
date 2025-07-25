package org.annill.deal.controller.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.annill.deal.controller.ContractorToRoleApi;
import org.annill.deal.dto.ContractorToRoleDto;
import org.annill.deal.service.ContractorToRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления связями контрагентов с ролями через UI. Предоставляет endpoints для сохранения и удаления
 * связей контрагент-роль.
 */
@RestController
@RequestMapping("ui/contractor-to-role")
@RequiredArgsConstructor
@Slf4j
public class UiContractorToRoleController implements ContractorToRoleApi {

    private final ContractorToRoleService contractorToRoleService;

    /**
     * Сохраняет связь контрагента с ролью.
     *
     * @param contractorToRoleDto DTO связи контрагент-роль
     * @return ResponseEntity с сохраненной связью
     */
    @PutMapping("/save")
    @PreAuthorize("hasAnyRole('DEAL_SUPERUSER', 'SUPERUSER')")
    public ResponseEntity<?> save(@RequestBody ContractorToRoleDto contractorToRoleDto) {
        log.info("Сохраняет связь контрагента с ролью: {}", contractorToRoleDto);
        contractorToRoleService.save(contractorToRoleDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет связь контрагента с ролью.
     *
     * @param contractorToRoleDto DTO связи контрагент-роль
     * @return ResponseEntity без содержимого
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('DEAL_SUPERUSER', 'SUPERUSER')")
    public ResponseEntity<?> delete(@RequestBody ContractorToRoleDto contractorToRoleDto) {
        log.info("Удаляет связь контрагента с ролью.: {}", contractorToRoleDto);
        contractorToRoleService.delete(contractorToRoleDto);
        return ResponseEntity.ok().build();
    }

}
