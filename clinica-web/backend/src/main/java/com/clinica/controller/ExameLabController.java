package com.clinica.controller;

import com.clinica.model.ExameLab;
import com.clinica.repository.ExameLabRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exames-lab")
@CrossOrigin(origins = "*")
public class ExameLabController {

    private final ExameLabRepository repository;

    public ExameLabController(ExameLabRepository repository) {
        this.repository = repository;
    }

    // CREATE - Inserir novo exame laboratorial
    @PostMapping
    public ResponseEntity<ExameLab> criar(@Valid @RequestBody ExameLab exameLab) {
        ExameLab salvo = repository.save(exameLab);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // READ - Listar todos ou consultar (por Descrição ou por Atendimento)
    @GetMapping
    public ResponseEntity<List<ExameLab>> listar(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Long atendimentoId) {

        if (descricao != null && !descricao.trim().isEmpty()) {
            return ResponseEntity.ok(repository.findByDescricaoContainingIgnoreCase(descricao));
        }
        if (atendimentoId != null) {
            return ResponseEntity.ok(repository.findByAtendimentoId(atendimentoId));
        }

        return ResponseEntity.ok(repository.findAll());
    }

    // READ - Consultar exame laboratorial por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // UPDATE - Alterar dados do exame laboratorial por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody ExameLab dados) {
        return repository.findById(id)
                .map(exame -> {
                    exame.setDescricao(dados.getDescricao());
                    exame.setAtendimento(dados.getAtendimento());
                    return ResponseEntity.ok(repository.save(exame));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Excluir exame laboratorial por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(exame -> {
                    repository.delete(exame);
                    return ResponseEntity.ok(Map.of("mensagem", "Exame laboratorial removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}