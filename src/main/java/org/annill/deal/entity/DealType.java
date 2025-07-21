package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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


}
