package br.com.diego.sct.infra.repository.interfaces;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import br.com.diego.sct.domain.entity.TbPayableEntry;
import br.com.diego.sct.domain.model.PayableEntry;
import br.com.diego.sct.infra.repository.Pagination;
import br.com.diego.sct.infra.repository.PayableEntryRepository;
import br.com.diego.sct.infra.utils.Util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Component
public class PayableEntryRepositoryImpl implements PayableEntryRepository {

    private final PayableEntryJpaRepository payableEntryJpaRepository;
    private final PayableEntryMapperRepository payableEntryMapperRepository;

    PayableEntryRepositoryImpl(PayableEntryJpaRepository payableEntryJpaRepository,
        PayableEntryMapperRepository payableEntryMapperRepository){

        this.payableEntryJpaRepository = payableEntryJpaRepository;
        this.payableEntryMapperRepository = payableEntryMapperRepository;
    }

    @Override
    public PayableEntry save(PayableEntry payableEntry) {
        log.info("{} Salvando no banco de dados uma nova conta a pagar: {}", Util.LOG_PREFIX, payableEntry.toString());
        TbPayableEntry tbPayableEntry = payableEntryMapperRepository.toTbPayableEntry(payableEntry);
        TbPayableEntry savedTbPayableEntry = payableEntryJpaRepository.save(tbPayableEntry);
        log.info("Conta salva com Sucesso! {}", savedTbPayableEntry);
        return payableEntryMapperRepository.toPayableEntry(savedTbPayableEntry);
    }

    @Override
    public Optional<PayableEntry> findById(UUID id) {
        log.info("{} Buscando no banco a conta a pagar pelo id: {}", Util.LOG_PREFIX, id);
        TbPayableEntry byId = payableEntryJpaRepository.findById(id).orElse(null);
        return Optional.ofNullable(payableEntryMapperRepository.toPayableEntry(byId));
    }

    @Override
    public Pagination<PayableEntry> findAllByFilters(Specification<TbPayableEntry> payableEntrySpecification,
        Pageable pageable) {
        log.info("{} Buscando no banco pelas specifications: {}", Util.LOG_PREFIX, payableEntrySpecification.toString());
        Page<TbPayableEntry> tbPayableEntries = payableEntryJpaRepository.findAll(payableEntrySpecification, pageable);
        return payableEntryMapperRepository.mapPageToPayableEntryPage(tbPayableEntries);
    }

    @Override public long saveAll(List<PayableEntry> payableEntryDtoToSave) {
        log.info("{} Salvando em lote no banco de dados. Quantidade: {}", Util.LOG_PREFIX, payableEntryDtoToSave.size());
        return payableEntryJpaRepository.saveAll(payableEntryDtoToSave.stream()
            .map(payableEntryMapperRepository::toTbPayableEntry)
            .toList()).size();
    }

    @Override
    public BigDecimal sumPaidAmountBetweenDates(LocalDate initialDate, LocalDate finalDate) {
        log.info("{} Buscando no banco valor total de contas pagas entre o período de {} a {}",
            Util.LOG_PREFIX, initialDate, finalDate);
        return payableEntryJpaRepository.sumPaidAmountBetweenDates(initialDate, finalDate);
    }
}