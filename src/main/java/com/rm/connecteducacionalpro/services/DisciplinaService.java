package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.escola.Disciplina;
import com.rm.connecteducacionalpro.model.dto.escola.DisciplinaDTO;

import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.repositories.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    @Autowired
    public DisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<Disciplina> getAllDisciplinas() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }

        return disciplinaRepository.findAllBySchoolId(school.getId());
    }

    public Optional<Disciplina> getDisciplinaById(Long id) {
        return disciplinaRepository.findById(id);
    }

    public Disciplina criarDisciplina(DisciplinaDTO disciplinaDTO) {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        Disciplina disciplina = new Disciplina(disciplinaDTO);
        disciplina.setId(null);
        disciplina.setSchool(school);
        return disciplinaRepository.save(disciplina);
    }

    public Disciplina atualizarDisciplina(Long id, DisciplinaDTO disciplinaDTO) {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        Optional<Disciplina> optionalDisciplina = disciplinaRepository.findById(id);

        if (optionalDisciplina.isPresent()) {
            Disciplina disciplina = optionalDisciplina.get();
            disciplina.setId(optionalDisciplina.get().getId());
            disciplina.setName(disciplinaDTO.getName());
            disciplina.setSchool(school);
            return disciplinaRepository.save(disciplina);
        } else {
            // Tratar caso a disciplina não seja encontrada
            return null;
        }
    }

    public void deletarDisciplina(Long id) {
        disciplinaRepository.deleteById(id);
    }
}
