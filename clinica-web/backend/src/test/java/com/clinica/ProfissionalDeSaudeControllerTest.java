package com.clinica;

import com.clinica.controller.ProfissionalDeSaudeController;
import com.clinica.model.ProfissionalDeSaude;
import com.clinica.repository.ProfissionalDeSaudeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * TESTES UNITÁRIOS - Profissionais de Saúde
 * Usa @WebMvcTest para testar apenas o controller isoladamente
 * O repository é mockado com @MockBean
 */
@WebMvcTest(ProfissionalDeSaudeController.class)
class ProfissionalDeSaudeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfissionalDeSaudeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarProfissionalComSucesso() throws Exception {
        ProfissionalDeSaude profissional = new ProfissionalDeSaude();
        profissional.setId(1L);
        profissional.setNome("Dr. João Silva");
        profissional.setTelefone("31999999999");
        profissional.setEndereco("Rua Alvarenga, 123");
        profissional.setCategoria("Médico");

        when(repository.save(any(ProfissionalDeSaude.class))).thenReturn(profissional);

        mockMvc.perform(post("/api/profissionais-saude")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profissional)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Dr. João Silva"))
                .andExpect(jsonPath("$.categoria").value("Médico"));
    }

    @Test
    void deveListarProfissionaisVazio() throws Exception {
        when(repository.findAllByOrderByNomeAsc()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/profissionais-saude"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar404ParaProfissionalInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profissionais-saude/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarProfissionalComSucesso() throws Exception {
        ProfissionalDeSaude profissional = new ProfissionalDeSaude();
        profissional.setId(1L);
        profissional.setNome("Dr. João Silva");

        when(repository.findById(1L)).thenReturn(Optional.of(profissional));

        mockMvc.perform(delete("/api/profissionais-saude/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Profissional de saúde removido com sucesso"));
    }
}