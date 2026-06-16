package com.clinica;

import com.clinica.controller.AtendimentoController;
import com.clinica.model.Atendimento;
import com.clinica.repository.AtendimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES UNITÁRIOS - Atendimentos (DEV 2 - Bruno)
 * Usa @WebMvcTest para testar apenas o controller isoladamente
 */
@WebMvcTest(AtendimentoController.class)
class AtendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtendimentoRepository repository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveCriarAtendimentoComSucesso() throws Exception {
        Atendimento atendimento = new Atendimento();
        atendimento.setId(1L);
        atendimento.setData(LocalDate.of(2026, 6, 16));
        atendimento.setHorario(LocalTime.of(14, 0));
        atendimento.setProblemaTexto("Check-up geral de rotina");

        when(repository.save(any(Atendimento.class))).thenReturn(atendimento);

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.problemaTexto").value("Check-up geral de rotina"));
    }

    @Test
    void deveListarAtendimentosOrdenados() throws Exception {
        Atendimento at1 = new Atendimento();
        at1.setId(1L);
        at1.setData(LocalDate.of(2026, 6, 16));
        at1.setHorario(LocalTime.of(9, 0));
        at1.setProblemaTexto("Primeira consulta");

        Atendimento at2 = new Atendimento();
        at2.setId(2L);
        at2.setData(LocalDate.of(2026, 6, 16));
        at2.setHorario(LocalTime.of(10, 0));
        at2.setProblemaTexto("Retorno de exames");

        when(repository.findAllByOrderByDataAscHorarioAsc())
                .thenReturn(Arrays.asList(at1, at2));

        mockMvc.perform(get("/api/atendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].problemaTexto").value("Primeira consulta"))
                .andExpect(jsonPath("$[1].problemaTexto").value("Retorno de exames"));
    }

    @Test
    void deveRetornar404ParaAtendimentoInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/atendimentos/999"))
                .andExpect(status().isNotFound());
    }
}