package com.rm.connecteducacionalpro.repositories;

import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Saida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaidaRepository extends JpaRepository<Saida, Long> {

    List<Saida> findByTeacher(Optional<Teacher> id);

    List<Saida> findByEmployee(Optional<Employee> id);
}
