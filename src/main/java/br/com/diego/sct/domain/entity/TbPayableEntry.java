package br.com.diego.sct.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import br.com.diego.sct.domain.enums.Status;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contas")
public class TbPayableEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dueDate;

    @Column(name = "data_pagamento")
    private LocalDate paymentDate;

    @Column(name = "valor", nullable = false)
    private BigDecimal value;

    @Column(name = "descricao", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private Status status;
}