package org.annill.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.annill.deal.dto.ContractorToRoleDto;
import org.annill.deal.service.ContractorToRoleService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST контроллер для управления связью между контрагентами и их ролями в сделках. Позволяет добавлять роли
 * существующим контрагентам сделки и удалять их.
 */

@Slf4j
@RestController
@RequestMapping("/contractor-to-role")
@AllArgsConstructor
public class ContractorToRoleController {

    private ContractorToRoleService contractorToRoleService;

    @PutMapping("/save")
    @Operation(summary ="Добавления роли существующему контрагенту сделки" )
    public void save(@RequestBody ContractorToRoleDto contractorToRoleDto) {
        log.info("Добавления роли существующему контрагенту сделки");
        contractorToRoleService.save(contractorToRoleDto);
    }

    @DeleteMapping("/delete")
    @Operation(summary ="Удаление роли у существующего контрагента сделки" )
    public void delete(@RequestBody ContractorToRoleDto contractorToRoleDto) {
        log.info("Удаление роли у существующего контрагента сделки");
        contractorToRoleService.delete(contractorToRoleDto);
    }

}
