package org.annill.deal.spec;


import jakarta.persistence.criteria.JoinType;
import org.annill.deal.entity.Deal;
import org.annill.deal.filter.DealSearchFilterDto;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class DealSpecifications {

    public static Specification<Deal> withFilter(DealSearchFilterDto filter) {
        return (root, query, cb) -> {
            var p = cb.conjunction();

            p = cb.and(p, cb.isTrue(root.get("isActive")));

            if (filter.getDealId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), UUID.fromString(filter.getDealId())));
            }
            if (filter.getDescription() != null) {
                p = cb.and(p, cb.equal(root.get("description"), filter.getDescription()));
            }
            if (filter.getAgreementNumber() != null) {
                p = cb.and(p, cb.like(root.get("agreementNumber"), "%" + filter.getAgreementNumber() + "%"));
            }
            if (filter.getAgreementDateFrom() != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("agreementDate"), filter.getAgreementDateFrom()));
            }
            if (filter.getAgreementDateTo() != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("agreementDate"), filter.getAgreementDateTo()));
            }
            if (filter.getAvailabilityDateFrom() != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("availabilityDate"), filter.getAvailabilityDateFrom()));
            }
            if (filter.getAvailabilityDateTo() != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("availabilityDate"), filter.getAvailabilityDateTo()));
            }
            if (filter.getCloseDtFrom() != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("closeDt"), filter.getCloseDtFrom().atStartOfDay()));
            }
            if (filter.getCloseDtTo() != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("closeDt"), filter.getCloseDtTo().atTime(23, 59, 59)));
            }
            if (filter.getType() != null && !filter.getType().isEmpty()) {
                p = cb.and(p, root.get("type").in(filter.getType()));
            }
            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                p = cb.and(p, root.get("status").in(filter.getStatus()));
            }

            if (filter.getBorrowerSearch() != null) {
                var contractorsJoin = root.join("dealContractors", JoinType.LEFT);
                p = cb.and(p,
                    cb.and(
                        cb.equal(contractorsJoin.get("group"), "BORROWER"),
                        cb.or(
                            cb.like(contractorsJoin.get("contractorId").as(String.class), "%" + filter.getBorrowerSearch() + "%"),
                            cb.like(contractorsJoin.get("name"), "%" + filter.getBorrowerSearch() + "%"),
                            cb.like(contractorsJoin.get("inn"), "%" + filter.getBorrowerSearch() + "%")
                        )
                    )
                );
            }

            if (filter.getWarranitySearch() != null) {
                var contractorsJoin = root.join("dealContractors", JoinType.LEFT);
                p = cb.and(p,
                    cb.and(
                        cb.equal(contractorsJoin.get("group"), "WARRANITY"),
                        cb.or(
                            cb.like(contractorsJoin.get("contractorId").as(String.class), "%" + filter.getWarranitySearch() + "%"),
                            cb.like(contractorsJoin.get("name"), "%" + filter.getWarranitySearch() + "%"),
                            cb.like(contractorsJoin.get("inn"), "%" + filter.getWarranitySearch() + "%")
                        )
                    )
                );
            }

            if (filter.getSum() != null) {
                var sumJoin = root.join("dealSums", JoinType.LEFT);
                if (filter.getSum().getValue() != null) {
                    p = cb.and(p, cb.equal(sumJoin.get("value").as(String.class), filter.getSum().getValue()));
                }
                if (filter.getSum().getCurrency() != null) {
                    p = cb.and(p, cb.equal(sumJoin.get("currency"), filter.getSum().getCurrency()));
                }
            }

            return p;
        };
    }
}
