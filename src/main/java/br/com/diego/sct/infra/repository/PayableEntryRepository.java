package br.com.diego.sct.infra.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayableEntryRepository {
    br.com.diego.sct.domain.model.PayableEntry save(br.com.diego.sct.domain.model.PayableEntry payableEntry);
    Optional<br.com.diego.sct.domain.model.PayableEntry> findById(UUID id);
    Pagination<br.com.diego.sct.domain.model.PayableEntry> findAllByFilters(Specification<br.com.diego.sct.domain.entity.TbPayableEntry> payableEntrySpecification, Pageable pageable);
    long saveAll(List<br.com.diego.sct.domain.model.PayableEntry> payableEntryDtoToSave);
    BigDecimal sumPaidAmountBetweenDates(LocalDate initialDate, LocalDate finalDate);
}