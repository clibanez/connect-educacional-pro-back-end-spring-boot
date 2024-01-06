package com.rm.connecteducacionalpro.model.escola;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rm.connecteducacionalpro.model.dto.escola.MatriculaDTO;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;

    private BigDecimal valorMatricula;

    @Column(name = "data_matricula")
    private LocalDate dataMatricula;



    public Matricula(MatriculaDTO obj) {
        this.id = obj.getId();
        this.student = obj.getStudentId();
        this.turma = obj.getTurmaId();
        this.valorMatricula = obj.getValorMatricula();
        this.dataMatricula = obj.getDataMatricula();
    }
}
