package br.com.diego.sct.domain.controller;


import br.com.diego.sct.domain.enums.Status;
import br.com.diego.sct.domain.error.FileLoadingException;
import br.com.diego.sct.domain.service.PayableEntryService;
import br.com.diego.sct.infra.dto.PayableEntryDto;
import br.com.diego.sct.infra.repository.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("api/contas")
public class PayableEntryController {
    private PayableEntryService payableEntryService;

    @GetMapping
    @Operation(summary = "Lista contas a pagar")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Pagination<PayableEntryDto>> getPayableEntryListFilter(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate initialDueDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate finalDueDate,
        @RequestParam(required = false) List<Status> status,
        @RequestParam(required = false) String description,
        Pageable pageable) {

        final Pagination<PayableEntryDto> payableEntryDtoPage = payableEntryService.findAllByFilters(
            initialDueDate, finalDueDate, status, description, pageable);

        return ResponseEntity.ok(payableEntryDtoPage);
    }

    @PostMapping
    @Operation(summary = "Cadastra conta a pagar")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PayableEntryDto> createPayableEntry(@RequestBody PayableEntryDto payableEntryDto) {

        PayableEntryDto savedPayableEntry = payableEntryService.create(payableEntryDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedPayableEntry.id())
            .toUri();
        return ResponseEntity.created(location).body(savedPayableEntry);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera conta a pagar")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PayableEntryDto> atualizarConta(@PathVariable UUID id, @RequestBody PayableEntryDto payableEntryDto) {

        PayableEntryDto payableEntryDtoUpdated = payableEntryService.updatePayableEntry(id, payableEntryDto);
        return ResponseEntity.ok(payableEntryDtoUpdated);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Recupa conta pelo ID")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PayableEntryDto> getPayableEntryById(@PathVariable UUID id) {

        return ResponseEntity.ok(payableEntryService.findById(id));
    }

    @PutMapping("/{id}/situacao")
    @Operation(summary = "Altera situação da conta a pagar")
    public ResponseEntity<PayableEntryDto> updateStatus(@PathVariable UUID id, @RequestParam String status) {

        return ResponseEntity.ok(payableEntryService.updatePayableEntryStatus(id, Status.valueOf(status)));
    }

    @GetMapping("/total-pago")
    @Operation(summary = "Valor total da conta a pagar")
    @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<BigDecimal> getTotalPaidAmount(
            @NotNull @RequestParam LocalDate initialDate,
            @NotNull @RequestParam LocalDate finalDate) {

        BigDecimal totalPaid = payableEntryService.getTotalPaidAmount(initialDate, finalDate);
        return ResponseEntity.ok(totalPaid);
    }

    @Operation(summary = "Importar lote de contas a pagar por arquivo CSV", description = "Utilize \",\" como separador de campos")
    @PostMapping(path = "/importar")
    public ResponseEntity<String> post(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) return ResponseEntity.ok("Arquivo vazio");

        try {
            long linesProcessed = payableEntryService.loadCsvFileContent(new String(file.getBytes()));
            return ResponseEntity.ok("Contas a pagar inseridas: " + linesProcessed);
        } catch (IOException e) {
            throw new FileLoadingException("Erro ao processar o arquivo");
        }
    }
}