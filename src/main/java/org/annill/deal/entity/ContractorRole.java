package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.annill.deal.ShortUUIDGenerator;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "contractor_role")
@Data
public class ContractorRole {

    @Id
    @GeneratedValue(generator = "short_uuid")
    @GenericGenerator(
        name = "short_uuid",
        type = ShortUUIDGenerator.class
    )
    @Column(
        name = "id",
        length = 30,
        updatable = false,
        nullable = false
    )
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
