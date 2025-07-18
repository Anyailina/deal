package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.annill.deal.ShortUUIDGenerator;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "deal_status")
@Data
@Accessors(chain = true)
public class DealStatus {

    @Id
    @GeneratedValue(generator = "short_uuid")
    @GenericGenerator(
        name = "short_uuid",
        type = ShortUUIDGenerator.class
    )
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active")
    private boolean isActive;


    @OneToMany(mappedBy = "status")
    @EqualsAndHashCode.Exclude
    private List<Deal> dealList;

    public DealStatus() {

    }

}

