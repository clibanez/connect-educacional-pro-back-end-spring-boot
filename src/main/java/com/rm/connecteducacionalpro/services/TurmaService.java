package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.dto.escola.DisciplinaDTO;
import com.rm.connecteducacionalpro.model.dto.escola.TurmaDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.TeacherDTO;
import com.rm.connecteducacionalpro.model.escola.Disciplina;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.Turma;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import com.rm.connecteducacionalpro.repositories.DisciplinaRepository;
import com.rm.connecteducacionalpro.repositories.TeacherRepository;
import com.rm.connecteducacionalpro.repositories.TurmaRepository;
import com.rm.connecteducacionalpro.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private TeacherRepository teacherRepository;




    public Turma findById(Long id){
        Optional<Turma> turma = turmaRepository.findById(id);
//        return occurrence.orElse(null);
        return turma.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Turma> findAllByTeacherSchoolId() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        return turmaRepository.findAllBySchoolId(school.getId());
    }

    public Turma criarTurmaComRelacionamentos(TurmaDTO turmaDTO) {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }

        Turma turma = new Turma(turmaDTO);

        List<Disciplina> disciplinas = disciplinaRepository.findAllById(
                turmaDTO.getDisciplinas().stream().map(DisciplinaDTO::getId).collect(Collectors.toList())
        );
        List<Teacher> teachers = teacherRepository.findAllById(
                turmaDTO.getTeachers().stream().map(TeacherDTO::getId).collect(Collectors.toList())
        );

        turma.setDisciplinas(disciplinas);
        turma.setTeachers(teachers);
        turma.setSchool(school);

        return turmaRepository.save(turma);
    }

}