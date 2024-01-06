package com.rm.connecteducacionalpro.repositories;

import com.rm.connecteducacionalpro.model.escola.Disciplina;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    List<Disciplina> findAllBySchoolId(Long schoolId);



}