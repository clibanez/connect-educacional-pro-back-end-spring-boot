package com.rm.connecteducacionalpro.repositories;

import com.rm.connecteducacionalpro.model.escola.Disciplina;
import com.rm.connecteducacionalpro.model.escola.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {


}