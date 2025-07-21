package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "contractor_to_role")
@Data
@Accessors(chain = true)
public class ContractorToRole {

    @EmbeddedId
    private ContractorToRoleId id;

    @MapsId("contractorId")
    @ManyToOne
    @JoinColumn(name = "contractor_id", nullable = false)
    private DealContractor contractor;

    @MapsId("roleId")
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private ContractorRole role;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

}
