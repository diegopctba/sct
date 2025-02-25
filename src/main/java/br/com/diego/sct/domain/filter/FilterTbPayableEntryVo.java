package br.com.diego.sct.domain.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import br.com.diego.sct.domain.entity.TbPayableEntry;
import br.com.diego.sct.domain.enums.Status;
import br.com.diego.sct.interfaces.specification.TbPayableEntrySpecs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(name = "Filtro contas a pagar", description = "Filtro utilizado para requisição de contas a pagar")
public class FilterTbPayableEntryVo {

    private UUID id;
    private LocalDate initialDueDate;
    private LocalDate finalDueDate;
    private LocalDate initialPaymentDate;
    private LocalDate finalPaymentDate;
    private BigDecimal value;
    private String description;
    private List<Status> status;
    private boolean interno;

    public static FilterTbPayableEntryVo buildFilter(final LocalDate initialDueDate, final LocalDate finalDueDate,
        final List<Status> status, final String description) {

        return FilterTbPayableEntryVo.builder()
            .initialDueDate(initialDueDate)
            .finalDueDate(finalDueDate)
            .status(status)
            .description(description)
            .build();
    }

    public Specification<TbPayableEntry> toSpecs() {
        final var specPeriodDueDate = getPeriodByDueDate();
        final var specPeriodPaymentDate = getPeriodByPaymentDate();
        final var specStatus = getSpecStatus();
        final var specDescription = getDescriptionSpec();

        return specPeriodDueDate
            .and(specPeriodPaymentDate)
            .and(specStatus)
            .and(specDescription);
    }

    private Specification<TbPayableEntry> getPeriodByDueDate() {
        if (initialDueDate != null && finalDueDate != null) {
            return TbPayableEntrySpecs.dateCreateBetween(initialDueDate, finalDueDate, "dueDate");
        }
        if (initialDueDate != null) {
            return TbPayableEntrySpecs.dateCreateGreaterOrEqual(initialDueDate, "dueDate");
        }
        if (finalDueDate != null) {
            return TbPayableEntrySpecs.dataCreateMinorOrEqual(finalDueDate, "dueDate");
        }
        return Specification.where(null);
    }

    private Specification<TbPayableEntry> getPeriodByPaymentDate() {
        if (initialPaymentDate != null && finalPaymentDate != null) {
            return TbPayableEntrySpecs.dateCreateBetween(initialPaymentDate, finalPaymentDate, "paymentDate");
        }
        if (initialPaymentDate != null) {
            return TbPayableEntrySpecs.dateCreateGreaterOrEqual(initialPaymentDate, "paymentDate");
        }
        if (finalPaymentDate != null) {
            return TbPayableEntrySpecs.dataCreateMinorOrEqual(finalPaymentDate, "paymentDate");
        }
        return Specification.where(null);
    }

    private Specification<TbPayableEntry> getSpecStatus() {
        if (CollectionUtils.isNotEmpty(status)) {
            return TbPayableEntrySpecs.statusIn(status);
        }
        return Specification.where(null);
    }

    private Specification<TbPayableEntry> getDescriptionSpec() {
        if (StringUtils.isBlank(description)) {
            return Specification.where(null);
        }
        return TbPayableEntrySpecs.description(description.trim());
    }
}