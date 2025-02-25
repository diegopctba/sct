package br.com.diego.sct.infra.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.diego.sct.domain.entity.TbPayableEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface PayableEntryJpaRepository extends JpaRepository<TbPayableEntry, UUID>, JpaSpecificationExecutor<TbPayableEntry> {

    @Query("select sum(t.value) from TbPayableEntry t where t.paymentDate between :initialDate and :finalDate and t.status = 'PAID'")
    BigDecimal sumPaidAmountBetweenDates(LocalDate initialDate, LocalDate finalDate);
}
