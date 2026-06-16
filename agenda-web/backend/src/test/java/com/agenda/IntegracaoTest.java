package com.agenda;

import com.agenda.model.Atendimento;
import com.agenda.model.ExameLab;
import com.agenda.model.Profissional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES DE INTEGRAÇÃO
 * Usa @SpringBootTest para carregar todo o contexto da aplicação
 * Testa a integração real entre Controller → Service → Repository → Banco
 * No CI, roda com PostgreSQL real via container
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveExecutarFluxoCompletoProfissional() throws Exception {
        // 1. CRIAR profissional
        Profissional prof = new Profissional();
        prof.setNome("Dr. Roberto Alencar");
        prof.setTelefone("31988881111");
        prof.setEmail("roberto.med@email.com");
        prof.setEndereco("Av. Contorno, 1000");
        prof.setCategoria("Cardiologista");

        MvcResult result = mockMvc.perform(post("/api/profissionals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prof)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Dr. Roberto Alencar"))
                .andExpect(jsonPath("$.categoria").value("Cardiologista"))
                .andReturn();

        Long id = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asLong();

        // 2. BUSCAR profissional criado
        mockMvc.perform(get("/api/profissionals/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("roberto.med@email.com"));

        // 3. ATUALIZAR profissional
        prof.setNome("Dr. Roberto Alencar Silva");
        prof.setEmail("roberto.silva@email.com");

        mockMvc.perform(put("/api/profissionals/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prof)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dr. Roberto Alencar Silva"));

        // 4. DELETAR profissional
        mockMvc.perform(delete("/api/profissionals/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void deveExecutarFluxoIntegradoDeAtendimentoEExameLaboratorial() throws Exception {
        // 1. Instanciar e cadastrar um Profissional de Saúde base
        Profissional cardiologista = new Profissional();
        cardiologista.setNome("Dra. Sandra Ramos");
        cardiologista.setCategoria("Cardiologista");
        cardiologista.setEmail("sandra.ramos@email.com");

        MvcResult profResult = mockMvc.perform(post("/api/profissionals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardiologista)))
                .andExpect(status().isCreated())
                .andReturn();

        Long profId = objectMapper.readTree(profResult.getResponse().getContentAsString())
                .get("id").asLong();

        // 2. Criar um Atendimento vinculado ao Profissional recém-criado
        String atendimentoJson = String.format("""
            {
                "data": "2026-06-20",
                "horario": "14:30",
                "problemaTexto": "Arritmia e dores recorrentes no peito.",
                "receitaSaude": "Tomar medicação X de 12 em 12 horas.",
                "profissionalDeSaude": {"id": %d}
            }
            """, profId);

        MvcResult atendimentoResult = mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(atendimentoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.problemaTexto").value("Arritmia e dores recorrentes no peito."))
                .andReturn();

        Long atendimentoId = objectMapper.readTree(atendimentoResult.getResponse().getContentAsString())
                .get("id").asLong();

        // 3. Solicitar e vincular um ExameLab a este Atendimento específico
        String exameJson = String.format("""
            {
                "descricao": "Eletrocardiograma de Esforço com monitoramento de ritmo",
                "atendimento": {"id": %d}
            }
            """, atendimentoId);

        mockMvc.perform(post("/api/exames-lab")
                .contentType(MediaType.APPLICATION_JSON)
                .content(exameJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Eletrocardiograma de Esforço com monitoramento de ritmo"));

        // 4. Consultar o endpoint de exames filtrando por atendimento para garantir a consistência relacional
        mockMvc.perform(get("/api/exames-lab").param("atendimentoId", atendimentoId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Eletrocardiograma de Esforço com monitoramento de ritmo"))
                .andExpect(jsonPath("$[0].atendimento.id").value(atendimentoId));
    }
}