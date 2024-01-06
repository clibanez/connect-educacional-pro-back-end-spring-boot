package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.model.dto.escola.MatriculaDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.StudentDTO;
import com.rm.connecteducacionalpro.model.escola.Matricula;
import com.rm.connecteducacionalpro.model.escola.Turma;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.repositories.MatriculaRepository;
import com.rm.connecteducacionalpro.repositories.StudentRepository;
import com.rm.connecteducacionalpro.repositories.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private StudentService studentService;



    public Matricula criarMatricula(UserDTO userDTO,
                                    StudentDTO studentDTO,
                                    MultipartFile file,
                                    MatriculaDTO matriculaDTO) {

        Student student = studentService.create(userDTO,studentDTO,file);

        Matricula matricula = new Matricula(matriculaDTO);
        matricula.setId(null);
        matricula.setStudent(studentRepository.getById(student.getId()));
        matricula.setTurma(turmaRepository.getById(matriculaDTO.getTurmaId().getId()));

        return matriculaRepository.save(matricula);
    }


//    public Matricula criarMatricula(MatriculaDTO matriculaDTO) {
//
//
//
//        Matricula matricula = new Matricula(matriculaDTO);
//        matricula.setId(null);
//        matricula.setStudent(studentRepository.getById(matriculaDTO.getStudentId().getId()));
//        matricula.setTurma(turmaRepository.getById(matriculaDTO.getTurmaId().getId()));
//
//        return matriculaRepository.save(matricula);
//    }
}
