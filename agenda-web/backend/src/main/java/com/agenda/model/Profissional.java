package com.agenda.model;



import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;



@Entity

@Table(name = "Profissionais")

@Data

@NoArgsConstructor

@AllArgsConstructor

public class Profissional {



    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;



    @NotBlank(message = "Nome é obrigatório") // Corrigido: 'message'

    @Column(length = 100, nullable = false)

    private String nome;



    @Column(length = 20)

    private String telefone;



    @Column(length = 100)

    private String email;



    @Column(length = 200)

    private String endereco;



    @Column(length = 100) // Corrigido: String simples para facilitar a ordenação

    private String categoria;



    @Column(name = "criado_em")

    private LocalDateTime criadoEm = LocalDateTime.now();

}