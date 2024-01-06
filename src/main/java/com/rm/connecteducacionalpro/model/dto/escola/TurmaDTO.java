package com.rm.connecteducacionalpro.model.dto.escola;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rm.connecteducacionalpro.model.dto.escola.DisciplinaDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.StudentDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.TeacherDTO;
import com.rm.connecteducacionalpro.model.escola.Disciplina;
import com.rm.connecteducacionalpro.model.escola.Turma;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TurmaDTO {

    private Long id;
    private String serie;
    private String turno;
    private String sala;


    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioAula;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate anoInicio;

    private List<TeacherDTO> teachers;
    private List<DisciplinaDTO> disciplinas;

    public TurmaDTO(Turma obj) {
        this.id = obj.getId();
        this.serie = obj.getSerie();
        this.turno = obj.getTurno();
        this.sala = obj.getSala();

        this.horarioAula = obj.getHorarioAula();
        this.anoInicio = obj.getAnoInicio();

        // Mapeie as entidades relacionadas para DTOs
        this.teachers = obj.getTeachers().stream().map(TeacherDTO::new).collect(Collectors.toList());
        this.disciplinas = obj.getDisciplinas().stream().map(DisciplinaDTO::new).collect(Collectors.toList());
    }

}
