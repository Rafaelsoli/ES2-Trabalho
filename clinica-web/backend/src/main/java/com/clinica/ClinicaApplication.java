package com.clinica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação Principal - Clinica Médica
 * Ponto de partida do ecossistema do backend para gestão de clínicas,
 * integrando Profissionais, Atendimentos e Exames Laboratoriais.
 */
@SpringBootApplication
public class ClinicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicaApplication.class, args);
    }
}