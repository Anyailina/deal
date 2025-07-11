package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class ContractorToRoleId implements Serializable {

    @Column(name = "contractor_id", nullable = false)
    private UUID contractorId;

    @Column(name = "role_id", length = 30, nullable = false)
    private String roleId;

    public ContractorToRoleId(UUID contractorId, String roleId) {
        this.contractorId = contractorId;
        this.roleId = roleId;
    }

}
