package com.agenda.controller;

import com.agenda.model.Profissional;
import com.agenda.repository.ProfissionalRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profissionais") // CORRIGIDO: Agora bate certinho com o frontend!
@CrossOrigin(origins = "*")
public class ProfissionalController {

    private final ProfissionalRepository repository;

    public ProfissionalController(ProfissionalRepository repository) {
        this.repository = repository;
    }

    // CREATE - Criar novo profissional
    @PostMapping
    public ResponseEntity<Profissional> criar(@Valid @RequestBody Profissional profissional) {
        Profissional salvo = repository.save(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // READ - Listar todos os profissionais por Nome
    @GetMapping
    public ResponseEntity<List<Profissional>> listar() {
        List<Profissional> profissionais = repository.findAllByOrderByNomeAsc();
        return ResponseEntity.ok(profissionais);
    }

    // READ - Listar todos os profissionais por Categoria
    @GetMapping("/categoria")
    public ResponseEntity<List<Profissional>> listarPorCategoria() {
        List<Profissional> profissionais = repository.findAllByOrderByCategoriaAsc();
        return ResponseEntity.ok(profissionais);
    }

    // READ - Buscar profissional por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // UPDATE - Atualizar profissional
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody Profissional dados) {
        return repository.findById(id)
                .map(profissional -> {
                    profissional.setNome(dados.getNome());
                    profissional.setTelefone(dados.getTelefone());
                    // CORRIGIDO: setEmail removido completamente daqui de dentro
                    profissional.setEndereco(dados.getEndereco());
                    profissional.setCategoria(dados.getCategoria());
                    return ResponseEntity.ok(repository.save(profissional));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Remover profissional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(profissional -> {
                    repository.delete(profissional);
                    return ResponseEntity.ok(Map.of("mensagem", "Profissional removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
