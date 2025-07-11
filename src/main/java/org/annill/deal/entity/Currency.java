package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Entity
@Table(name = "currency")
@Data
@Accessors(chain = true)
public class Currency {

    @Id
    @Column(length = 3, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "currency")
    @EqualsAndHashCode.Exclude
    private List<DealSum> dealSumList;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 1000);
            this.id = String.format("%03d", randomNum);
        }
    }

}
