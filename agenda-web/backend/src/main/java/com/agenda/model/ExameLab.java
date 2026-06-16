package com.agenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "exames_lab")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExameLab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "atendimento_id", nullable = false)
    private Atendimento atendimento;
}