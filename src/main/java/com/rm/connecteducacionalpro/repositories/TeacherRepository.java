package com.rm.connecteducacionalpro.repositories;

import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TeacherRepository  extends JpaRepository<Teacher, Long> {

    @Transactional(readOnly = true)
    List<Teacher> findByUserSchool(School schoolId);
}
