package com.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "atendimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @NotNull(message = "Horário é obrigatório")
    private LocalTime horario;

    @Column(name = "problema_texto", columnDefinition = "TEXT")
    private String problemaTexto;

    @Column(name = "receita_saude", columnDefinition = "TEXT")
    private String receitaSaude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_saude_id", nullable = false)
    private ProfissionalDeSaude profissionalDeSaude;
}