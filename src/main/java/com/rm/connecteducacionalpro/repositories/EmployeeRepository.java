package com.rm.connecteducacionalpro.repositories;

import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

//    @Query("SELECT e FROM Employee e WHERE e.user.defesaCivil = :defesaCivil")
//    List<Employee> findEmployeesByDefesaCivil(@Param("defesaCivil") DefesaCivil defesaCivil);


    @Transactional(readOnly = true)
    List<Employee> findByUserSchool(School schoolId);

    @Transactional(readOnly = true)
    Employee findByImageName(String image);



}