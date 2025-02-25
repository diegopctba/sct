package br.com.diego.sct.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

import br.com.diego.sct.domain.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Getter
@AllArgsConstructor
public class PayableEntry {

    @NotNull
    private UUID id;

    @Setter
    @NotBlank
    private String description;

    @Setter
    @NotNull
    @FutureOrPresent
    private LocalDate dueDate;

    private LocalDate paymentDate;

    @NotNull
    private Status status;

    @Setter
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal value;

    public void validate() {
        List<String> errorList = new ArrayList<>();
        if (description == null || description.isBlank()) {
            errorList.add("Descrição não pode ser vazia");
        }
        if (dueDate == null || dueDate.isBefore(LocalDate.now()) && status != Status.PENDING_APPROVAL_OVERDUE) {
            errorList.add("Data de vencimento não pode ser nula ou no passado");
        }
        if (paymentDate != null && paymentDate.isAfter(LocalDate.now())) {
            errorList.add("Data de pagamento não poder ser no futuro");
        }
        if (paymentDate != null && paymentDate.isAfter(dueDate) && status != Status.PENDING_APPROVAL_OVERDUE) {
            errorList.add("Esta conta está atrasada e precisa de autorização para ser quitada");
        }
        if (status == null) {
            errorList.add("O status não pode ser nulo");
        }
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            errorList.add("O valor deve ser maior que zero");
        }
        if (!errorList.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errorList));
        }
    }

    private boolean isValidatePaymentDate(LocalDate paymentDate) {
        return paymentDate != null && paymentDate.isBefore(dueDate);
    }

    public void setStatusIfValid(Status newStatus) {
        if (newStatus.equals(this.status)) {
            return;
        }

        switch (newStatus) {
            case OPEN:
            case PENDING_APPROVAL_OVERDUE:
            case CANCELLED:
                this.status = newStatus;
                break;
            case APPROVED:
                this.status = newStatus;
                this.setStatusIfValid(Status.PAID);
                break;
            case PAID:
                if (this.status == Status.PENDING_APPROVAL_OVERDUE) {
                    throw new IllegalArgumentException("Esta conta precisa de aprovação para ser paga.");
                }
                if (this.status == Status.CANCELLED) {
                    throw new IllegalArgumentException("Esta conta já está cancelada.");
                }
                LocalDate paymentDateCandidate = LocalDate.now();
                if (isValidatePaymentDate(paymentDateCandidate)) {
                    this.paymentDate = paymentDateCandidate;
                    this.status = Status.PAID;
                } else {
                    this.status = Status.PENDING_APPROVAL_OVERDUE;
                }
                break;
            case REJECTED:
                this.status = Status.CANCELLED;
                break;
            default:
                throw new IllegalArgumentException("Status inválido.");
        }
    }

    public void setPaymentDate(LocalDate paymentDate) {
        if (paymentDate == null || paymentDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de pagamento não pode ser no futuro");
        }
        if (status == Status.PAID || status == Status.CANCELLED) {
            this.paymentDate = paymentDate;
            return;
        }
        if (isValidatePaymentDate(paymentDate)) {
            this.paymentDate = paymentDate;
        } else {
            this.status = Status.PENDING_APPROVAL_OVERDUE;
        }
    }
}