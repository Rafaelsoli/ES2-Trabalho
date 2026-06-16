package com.agenda.repository;

import com.agenda.model.ExameLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExameLabRepository extends JpaRepository<ExameLab, Long> {
    
    // Busca exames laboratoriais que contenham parte da descrição informada (ignorando maiúsculas/minúsculas)
    List<ExameLab> findByDescricaoContainingIgnoreCase(String descricao);

    // Busca exames laboratoriais vinculados a um atendimento específico pelo ID do atendimento
    List<ExameLab> findByAtendimentoId(Long atendimentoId);
}