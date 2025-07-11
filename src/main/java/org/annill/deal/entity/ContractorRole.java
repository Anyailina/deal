package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "contractor_role")
@Data
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

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            String fullUuid = UUID.randomUUID().toString().replace("-", "");
            this.id = fullUuid.substring(0, 30);
        }
    }

}
