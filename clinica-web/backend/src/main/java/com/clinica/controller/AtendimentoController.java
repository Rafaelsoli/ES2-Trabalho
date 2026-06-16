package com.clinica.controller;

import com.clinica.model.Atendimento;
import com.clinica.repository.AtendimentoRepository;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/atendimentos")
@CrossOrigin(origins = "*")
public class AtendimentoController {

    private final AtendimentoRepository repository;

    public AtendimentoController(AtendimentoRepository repository) {
        this.repository = repository;
    }

    // CREATE - Inserir novo atendimento
    @PostMapping
    public ResponseEntity<Atendimento> criar(@Valid @RequestBody Atendimento atendimento) {
        Atendimento salvo = repository.save(atendimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // READ - Listar todos ou consultar (por Data ou por Profissional)
    @GetMapping
    public ResponseEntity<List<Atendimento>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long profissionalId) {
        
        if (data != null) {
            return ResponseEntity.ok(repository.findByDataOrderByHorarioAsc(data));
        }
        if (profissionalId != null) {
            return ResponseEntity.ok(repository.findByProfissionalDeSaudeId(profissionalId));
        }
        
        return ResponseEntity.ok(repository.findAllByOrderByDataAscHorarioAsc());
    }

    // READ - Consultar atendimento por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // UPDATE - Alterar dados do atendimento por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody Atendimento dados) {
        return repository.findById(id)
                .map(atendimento -> {
                    atendimento.setData(dados.getData());
                    atendimento.setHorario(dados.getHorario());
                    atendimento.setProblemaTexto(dados.getProblemaTexto());
                    atendimento.setReceitaSaude(dados.getReceitaSaude());
                    atendimento.setProfissionalDeSaude(dados.getProfissionalDeSaude());
                    return ResponseEntity.ok(repository.save(atendimento));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Excluir atendimento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(atendimento -> {
                    repository.delete(atendimento);
                    return ResponseEntity.ok(Map.of("mensagem", "Atendimento removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}