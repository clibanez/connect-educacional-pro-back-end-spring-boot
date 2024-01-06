package com.rm.connecteducacionalpro.repositories;

import com.rm.connecteducacionalpro.model.escola.Turma;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Saida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    List<Turma> findAllBySchoolId(Long schoolId);


}
