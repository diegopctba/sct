package br.com.diego.sct.interfaces.specification;

import org.springframework.data.jpa.domain.Specification;

import br.com.diego.sct.domain.entity.TbPayableEntry;
import br.com.diego.sct.domain.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TbPayableEntrySpecs {
    public static Specification<TbPayableEntry> hasDueDateBefore(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("dueDate"), date);
    }
    public static Specification<TbPayableEntry> hasAmountGreaterThan(BigDecimal amount) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("amount"), amount);
    }

    public static Specification<TbPayableEntry> description(String description) {
        return (root, query, builder) -> builder.like(builder.upper(root.get("description")), "%" + description.toUpperCase() + "%");
    }

    public static Specification<TbPayableEntry> statusIn(final List<Status> statuses) {
        return (root, query, builder) -> root.get("status").in(statuses);
    }

    public static Specification<TbPayableEntry> dateCreateBetween(LocalDate initialDate, LocalDate finalDate,
        String field) {
        return (root, query, builder) -> builder
            .between(root.get(field), initialDate, finalDate);
    }

    public static Specification<TbPayableEntry> dateCreateGreaterOrEqual(LocalDate initialDate, String field) {
        return (root, query, builder) -> builder
            .greaterThanOrEqualTo(root.get(field), initialDate);
    }

    public static Specification<TbPayableEntry> dataCreateMinorOrEqual(LocalDate finalDate, String field) {
        return (root, query, builder) -> builder
            .lessThanOrEqualTo(root.get(field), finalDate);
    }

    public static Specification<TbPayableEntry> distinct() {
        return (root, query, builder) -> { query.distinct(true); return null; };
    }

    public static Specification<TbPayableEntry> paymentDate(final LocalDateTime initialDate,
        final LocalDateTime finalDate) {
        return (root, query, builder) -> {
            if (Objects.isNull(initialDate)) {
                return builder.lessThanOrEqualTo(root.get("paymentDate"), finalDate);
            } else if (Objects.isNull(finalDate)) {
                return builder.greaterThanOrEqualTo(root.get("paymentDate"), initialDate);
            } else {
                return builder.between(root.get("paymentDate"), initialDate, finalDate);
            }
        };
    }

    public static Specification<TbPayableEntry> dueDate(final LocalDate dataInicial, final LocalDate dataFinal) {
        return (root, query, builder) -> {
            if (Objects.isNull(dataInicial)) {
                return builder.lessThanOrEqualTo(root.get("dueDate"), dataFinal);
            } else if (Objects.isNull(dataFinal)) {
                return builder.greaterThanOrEqualTo(root.get("dueDate"), dataInicial);
            } else {
                return builder.between(root.get("dueDate"), dataInicial, dataFinal);
            }
        };
    }

    public static Specification<TbPayableEntry> isNotCanceledOrFinished() {
        return (root, query, builder) -> builder
            .equal(root.get("status"), false);
    }
}
