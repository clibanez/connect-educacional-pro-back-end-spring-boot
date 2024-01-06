package com.rm.connecteducacionalpro.model.escola;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.connecteducacionalpro.model.dto.escola.TurmaDTO;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String serie;
    private String turno;
    private String sala;


    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioAula;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate anoInicio;


    @ManyToMany
    @JoinTable(
            name = "turma_teacher",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private List<Teacher> teachers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "turma_disciplina",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_id"))
    private List<Disciplina> disciplinas = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "turma")
    private List<Matricula> matriculas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;


    public Turma(TurmaDTO obj) {
        this.id = obj.getId();
        this.serie = obj.getSerie();
        this.turno = obj.getTurno();
        this.sala = obj.getSala();

        this.horarioAula = obj.getHorarioAula();
        this.anoInicio = obj.getAnoInicio();

        // Mapeie os DTOs para as entidades relacionadas
        this.teachers = obj.getTeachers().stream().map(Teacher::new).collect(Collectors.toList());
        this.disciplinas = obj.getDisciplinas().stream().map(Disciplina::new).collect(Collectors.toList());
    }



}
