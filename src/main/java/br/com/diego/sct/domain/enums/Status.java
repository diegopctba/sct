package br.com.diego.sct.domain.enums;

import lombok.Getter;

@Getter
public enum Status {
    OPEN("Aguardando pagamento"),
    PENDING_APPROVAL_OVERDUE("Aguardando aprovação para pagamento"),
    APPROVED("Pagamento aprovado"),
    REJECTED("Reprovado"),
    PAID("Conta paga"),
    CANCELLED("Conta cancelada");

    private final String description;

    Status(final String description) {
        this.description = description;
    }
}
