package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Entity
@Table(name = "contractor_role")
@Data
@Accessors(chain = true)
public class ContractorRole {

    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String category;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "role")
    @EqualsAndHashCode.Exclude
    private List<ContractorToRole> contractorToRoleList;

}
