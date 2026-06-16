package com.agenda;

import com.agenda.controller.ExameLabController;
import com.agenda.model.Atendimento;
import com.agenda.model.ExameLab;
import com.agenda.repository.ExameLabRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES UNITÁRIOS - Exames Laboratoriais (DEV 2 - Bruno)
 * Usa @WebMvcTest para testar apenas o controller de forma isolada com Mockito
 */
@WebMvcTest(ExameLabController.class)
class ExameLabControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExameLabRepository repository;

    private ObjectMapper objectMapper;
    private Atendimento atendimentoMock;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Instancia um atendimento base fictício para satisfazer a obrigatoriedade da FK
        atendimentoMock = new Atendimento();
        atendimentoMock.setId(1L);
    }

    @Test
    void deveCriarExameLabComSucesso() throws Exception {
        ExameLab exame = new ExameLab();
        exame.setId(1L);
        exame.setDescricao("Hemograma Completo");
        exame.setAtendimento(atendimentoMock);

        when(repository.save(any(ExameLab.class))).thenReturn(exame);

        mockMvc.perform(post("/api/exames-lab")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Hemograma Completo"));
    }

    @Test
    void deveListarTodosOsExamesSemFiltro() throws Exception {
        ExameLab ex1 = new ExameLab(1L, "Hemograma", atendimentoMock);
        ExameLab ex2 = new ExameLab(2L, "Glicemia em Jejum", atendimentoMock);

        when(repository.findAll()).thenReturn(Arrays.asList(ex1, ex2));

        mockMvc.perform(get("/api/exames-lab"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Hemograma"))
                .andExpect(jsonPath("$[1].descricao").value("Glicemia em Jejum"));
    }

    @Test
    void deveConsultarExamesPorDescricao() throws Exception {
        ExameLab ex = new ExameLab(1L, "Perfil Lipídico Completo", atendimentoMock);

        when(repository.findByDescricaoContainingIgnoreCase("Lipídico"))
                .thenReturn(Arrays.asList(ex));

        mockMvc.perform(get("/api/exames-lab").param("descricao", "Lipídico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Perfil Lipídico Completo"));
    }

    @Test
    void deveConsultarExamesPorAtendimentoVinculado() throws Exception {
        ExameLab ex = new ExameLab(1L, "Raio-X de Tórax", atendimentoMock);

        when(repository.findByAtendimentoId(1L)).thenReturn(Arrays.asList(ex));

        mockMvc.perform(get("/api/exames-lab").param("atendimentoId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Raio-X de Tórax"));
    }

    @Test
    void deveBuscarExamePorIdComSucesso() throws Exception {
        ExameLab exame = new ExameLab(1L, "Uréia e Creatinina", atendimentoMock);

        when(repository.findById(1L)).thenReturn(Optional.of(exame));

        mockMvc.perform(get("/api/exames-lab/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Uréia e Creatinina"));
    }

    @Test
    void deveRetornar404ParaExameInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/exames-lab/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAlterarExamePorIdComSucesso() throws Exception {
        ExameLab exameExistente = new ExameLab(1L, "Descrição Antiga", atendimentoMock);
        ExameLab exameAtualizado = new ExameLab(1L, "Descrição Atualizada", atendimentoMock);

        when(repository.findById(1L)).thenReturn(Optional.of(exameExistente));
        when(repository.save(any(ExameLab.class))).thenReturn(exameAtualizado);

        mockMvc.perform(put("/api/exames-lab/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exameAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Descrição Atualizada"));
    }

    @Test
    void deveExcluirExamePorIdComSucesso() throws Exception {
        ExameLab exame = new ExameLab(1L, "Exame para deletar", atendimentoMock);

        when(repository.findById(1L)).thenReturn(Optional.of(exame));

        mockMvc.perform(delete("/api/exames-lab/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Exame laboratoriais removido com sucesso"));
    }
}