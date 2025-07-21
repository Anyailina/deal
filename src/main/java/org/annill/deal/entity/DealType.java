package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "deal_type")
@Accessors(chain = true)
public class DealType {

    @Id
    @Column(length = 3, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "type")
    @EqualsAndHashCode.Exclude
    private List<Deal> dealList;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            String fullUuid = UUID.randomUUID().toString().replace("-", "");
            this.id = fullUuid.substring(0, 30);
        }
    }

}
