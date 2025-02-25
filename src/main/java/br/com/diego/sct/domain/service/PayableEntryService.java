package br.com.diego.sct.domain.service;


import br.com.diego.sct.domain.enums.Status;
import br.com.diego.sct.domain.error.FileProcessException;
import br.com.diego.sct.domain.error.PayableEntryNotFoundException;
import br.com.diego.sct.domain.filter.FilterTbPayableEntryVo;
import br.com.diego.sct.domain.mapper.PayableEntryMapper;
import br.com.diego.sct.domain.model.PayableEntry;
import br.com.diego.sct.infra.dto.PayableEntryDto;
import br.com.diego.sct.infra.repository.Pagination;
import br.com.diego.sct.infra.repository.PayableEntryRepository;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


@Service
@AllArgsConstructor
public class PayableEntryService {

    private PayableEntryRepository payableEntryRepository;
    private PayableEntryMapper payableEntryMapper;

    public PayableEntryDto create(PayableEntryDto payableEntryDto) {

        final PayableEntry payableEntry = payableEntryMapper.toPayableEntry(payableEntryDto);
        payableEntry.validate();
        final PayableEntry savedPayableEntry = payableEntryRepository.save(payableEntry);
        return payableEntryMapper.toPayableEntryDto(savedPayableEntry);
    }

    public PayableEntryDto updatePayableEntry(UUID id, PayableEntryDto payableEntryDto) {

        Optional<PayableEntry> optionalPayableEntry = payableEntryRepository.findById(id);
        return optionalPayableEntry.map(atualPayableEntry -> {
                atualPayableEntry.setDescription(payableEntryDto.description());
                atualPayableEntry.setDueDate(payableEntryDto.dueDate());
                atualPayableEntry.setPaymentDate(payableEntryDto.paymentDate());
                atualPayableEntry.setStatusIfValid(payableEntryDto.status());
                atualPayableEntry.setValue(payableEntryDto.value());
                atualPayableEntry.validate();
                return payableEntryMapper.toPayableEntryDto(payableEntryRepository.save(atualPayableEntry));
            })
            .orElseThrow(PayableEntryNotFoundException.with(id, "id"));
    }

    public PayableEntryDto updatePayableEntryStatus(UUID id, Status status) {
        Optional<PayableEntry> optionalPayableEntry = payableEntryRepository.findById(id);
        return optionalPayableEntry.map(payableEntry -> {
            payableEntry.setStatusIfValid(status);
            return payableEntryMapper.toPayableEntryDto(payableEntryRepository.save(payableEntry));
        }).orElseThrow(PayableEntryNotFoundException.with(id, "id"));
    }

    public PayableEntryDto findById(final UUID id) throws PayableEntryNotFoundException {

        Optional<PayableEntry> optionalPayableEntry = payableEntryRepository.findById(id);
        return optionalPayableEntry
            .map(payableEntry -> payableEntryMapper.toPayableEntryDto(payableEntry))
            .orElseThrow(PayableEntryNotFoundException.with(id, "id"));
    }

    public Pagination<PayableEntryDto> findAllByFilters(final LocalDate initialDueDate, final LocalDate finalDueDate,
        final List<Status> status, final String description, final Pageable pageable) {

        final var filter = FilterTbPayableEntryVo.buildFilter(initialDueDate, finalDueDate, status, description);

        var payableEntrySpecification = filter.toSpecs();
        return payableEntryRepository.findAllByFilters(payableEntrySpecification, pageable)
            .map(payableEntryMapper::toPayableEntryDto);
    }

    public long loadCsvFileContent(String content) {

        var lineCount = 1L;
        try {
            List<PayableEntry> payableEntryDtoToSave = new ArrayList<>();
            String[] rows = content.split("\n");
            for (String row : rows) {
                String[] columns = row.split(",");
                PayableEntry payableEntry = new PayableEntry(
                    null,
                    columns[0].trim(),
                    getParseLocalDate(columns[1]),
                    getParseLocalDate(columns[2]),
                    Status.valueOf(columns[3]),
                    new BigDecimal(columns[4])
                );
                payableEntry.validate();
                payableEntryDtoToSave.add(payableEntry);
                lineCount++;
            }
            return payableEntryRepository.saveAll(payableEntryDtoToSave);
        } catch (Exception e) {
            var message = String.format("Erro ao processar linha %s. Erro: %s", lineCount, e.getMessage());
            throw new FileProcessException(message);

        }
    }

    public BigDecimal getTotalPaidAmount(LocalDate initialDate, LocalDate finalDate) {
        return payableEntryRepository.sumPaidAmountBetweenDates(initialDate, finalDate);
    }

    private static LocalDate getParseLocalDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        return LocalDate.parse(date);
    }
}