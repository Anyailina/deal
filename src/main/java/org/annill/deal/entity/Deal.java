package org.annill.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "deal")
@Data
@Accessors(chain = true)
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "uuid", nullable = false)
    private UUID id;

    private String description;

    @Column(name = "agreement_number")
    private String agreementNumber;

    @Column(name = "agreement_date")
    private LocalDate agreementDate;

    @Column(name = "agreement_start_dt")
    private LocalDateTime agreementStartDt;

    @Column(name = "availability_date")
    private LocalDate availabilityDate;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private DealType type;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private DealStatus status;

    @Column(name = "close_dt")
    private LocalDateTime closeDt;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column(name = "create_user_id")
    private String createUserId;

    @Column(name = "modify_user_id")
    private String modifyUserId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "deal")
    @EqualsAndHashCode.Exclude
    private List<DealSum> dealSumList;

    @OneToMany(mappedBy = "deal")
    @EqualsAndHashCode.Exclude
    private List<DealContractor> dealContractorList;

}
