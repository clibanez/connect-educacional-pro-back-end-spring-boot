package com.rm.connecteducacionalpro.repositories;

import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Mensalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long> {

//    List<Mensalidade> findByStudent(Student studentId);

    List<Mensalidade> findByStudent(Optional<Student> id);
}
