package com.agenda.repository;



import com.agenda.model.Profissional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;



@Repository

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

   

    // Busca e ordena pelo atributo 'nome'

    List<Profissional> findAllByOrderByNomeAsc();



    // Busca e ordena pelo atributo 'categoria' (Corrigido de 'Cate' para 'Categoria')

    List<Profissional> findAllByOrderByCategoriaAsc();

}