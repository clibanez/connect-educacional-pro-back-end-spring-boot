package com.rm.connecteducacionalpro.repositories;

import com.rm.connecteducacionalpro.model.escola.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findByName(String defesaCivilName);
}
