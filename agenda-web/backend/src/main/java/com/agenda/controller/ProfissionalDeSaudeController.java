package com.agenda.controller;

import com.agenda.model.ProfissionalDeSaude;
import com.agenda.repository.ProfissionalDeSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profissionais-saude")
@CrossOrigin(origins = "*")
public class ProfissionalDeSaudeController {

    private final ProfissionalDeSaudeRepository repository;

    public ProfissionalDeSaudeController(ProfissionalDeSaudeRepository repository) {
        this.repository = repository;
    }

    // CREATE - Inserir novo profissional de saúde
    @PostMapping
    public ResponseEntity<ProfissionalDeSaude> criar(@Valid @RequestBody ProfissionalDeSaude profissional) {
        ProfissionalDeSaude salvo = repository.save(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // READ - Listar e consultar (por Nome ou Categoria)
    @GetMapping
    public ResponseEntity<List<ProfissionalDeSaude>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria) {
        
        if (nome != null) {
            return ResponseEntity.ok(repository.findByNomeContainingIgnoreCase(nome));
        }
        if (categoria != null) {
            return ResponseEntity.ok(repository.findByCategoriaIgnoreCase(categoria));
        }
        
        return ResponseEntity.ok(repository.findAllByOrderByNomeAsc());
    }

    // READ - Buscar profissional por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // UPDATE - Alterar dados do profissional
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody ProfissionalDeSaude dados) {
        return repository.findById(id)
                .map(profissional -> {
                    profissional.setNome(dados.getNome());
                    profissional.setTelefone(dados.getTelefone());
                    profissional.setEndereco(dados.getEndereco());
                    profissional.setCategoria(dados.getCategoria());
                    return ResponseEntity.ok(repository.save(profissional));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Excluir profissional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(profissional -> {
                    repository.delete(profissional);
                    return ResponseEntity.ok(Map.of("mensagem", "Profissional de saúde removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}