package com.agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação Principal - Agenda Médica
 * Ponto de partida do ecossistema do backend para gestão de clínicas,
 * integrando Profissionais, Atendimentos e Exames Laboratoriais.
 */
@SpringBootApplication
public class AgendaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgendaApplication.class, args);
    }
}