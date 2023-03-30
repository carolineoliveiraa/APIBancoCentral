package com.jessicaoliveira.taxajuros.repository;

import com.jessicaoliveira.taxajuros.model.DadosModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface DadosRepository extends JpaRepository<DadosModel, Long> {
    List<DadosModel> findByAnoMes(String anoMes);

}
