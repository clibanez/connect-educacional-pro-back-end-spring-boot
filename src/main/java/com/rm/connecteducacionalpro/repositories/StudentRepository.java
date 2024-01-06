package com.rm.connecteducacionalpro.repositories;

import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long > {


    @Transactional(readOnly = true)
    List<Student> findAllByMatriculas_Turma_Id(Long turmaId);  //Lista todos os estudantes de uma turma

    @Transactional(readOnly = true)
    List<Student> findByUserSchool(School schoolId);



    @Transactional(readOnly = true)
    List<Student> findByUserSchoolAndDeletedFalse(School school); //Soft delete para estudante


}
