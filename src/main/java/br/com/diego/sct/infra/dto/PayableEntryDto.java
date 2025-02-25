package br.com.diego.sct.infra.dto;


import br.com.diego.sct.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PayableEntryDto(
    @Schema(description = "O identificador UUID único da conta a pagar.", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    UUID id,
    @Schema(description = "Descrição da conta a pagar.", example = "Conta de Energia")
    String description,
    @Schema(description = "Data de vencimento.", example = "2024-07-21")
    LocalDate dueDate,
    @Schema(description = "Data de pagamento.", example = "2024-07-20")
    LocalDate paymentDate,
    @Schema(description = "O situação da conta a pagar.", allowableValues = "OPEN, PENDING_APPROVAL_OVERDUE, PAID, CANCELLED")
    Status status,
    @Schema(description = "Valor da conta a pagar.", example = "12345.00")
    BigDecimal value
){}