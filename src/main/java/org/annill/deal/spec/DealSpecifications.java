package org.annill.deal.spec;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.UUID;
import org.annill.deal.entity.Deal;
import org.annill.deal.filter.DealSearchFilterDto;
import org.springframework.data.jpa.domain.Specification;

/**
 * Kласс для поиска сделок по фильтру.
 */
public final class DealSpecifications {

    /**
     * Приватный конструктор для предотвращения создания экземпляра утилитарного класса.
     */
    private DealSpecifications() {
        throw new UnsupportedOperationException();
    }

    /**
     * Создает спецификацию для сущности на основе переданного фильтра.
     * @param filter фильтр для поиска сделок.
     */
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
                p = cb.and(p, root.get("type").get("id").in(filter.getType()));
            }
            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                p = cb.and(p, root.get("status").get("id").in(filter.getStatus()));
            }
            if (filter.getBorrowerSearch() != null) {
                var contractorsJoin = root.join("dealContractorList", JoinType.LEFT);
                var contractorToRoleJoin = contractorsJoin.join("contractorToRoleList", JoinType.LEFT);
                var roleJoin = contractorToRoleJoin.join("role", JoinType.LEFT);

                p = cb.and(p,
                    cb.and(
                        cb.equal(roleJoin.get("category"), "BORROWER"),
                        cb.or(
                            cb.like(contractorsJoin.get("contractorId"), "%" + filter.getBorrowerSearch() + "%"),
                            cb.like(contractorsJoin.get("name"), "%" + filter.getBorrowerSearch() + "%"),
                            cb.like(contractorsJoin.get("inn"), "%" + filter.getBorrowerSearch() + "%")
                        )
                    )
                );
            }
            if (filter.getWarranitySearch() != null) {
                var contractorsJoin = root.join("dealContractorList", JoinType.LEFT);
                var contractorToRoleJoin = contractorsJoin.join("contractorToRoleList", JoinType.LEFT);
                var roleJoin = contractorToRoleJoin.join("role", JoinType.LEFT);

                p = cb.and(p,
                    cb.and(
                        cb.equal(roleJoin.get("category"), "WARRANTY"),
                        cb.or(
                            cb.like(contractorsJoin.get("contractorId"), "%" + filter.getWarranitySearch() + "%"),
                            cb.like(contractorsJoin.get("name"), "%" + filter.getWarranitySearch() + "%"),
                            cb.like(contractorsJoin.get("inn"), "%" + filter.getWarranitySearch() + "%")
                        )
                    )
                );
            }

            var sumJoin = root.join("dealSumList", JoinType.LEFT);

            if (filter.getSum() != null) {
                Predicate sumPredicate = null;

                if (filter.getSum().getValue() != null) {
                    sumPredicate = cb.equal(sumJoin.get("sum"), filter.getSum().getValue());
                }

                if (filter.getSum().getCurrency() != null) {
                    Predicate currencyPredicate = cb.equal(sumJoin.get("currency").get("id"),
                        filter.getSum().getCurrency());
                    sumPredicate = (sumPredicate == null) ? currencyPredicate : cb.or(sumPredicate, currencyPredicate);
                }

                if (sumPredicate != null) {
                    p = cb.and(p, sumPredicate);
                }
            }

            return p;
        };
    }

}
