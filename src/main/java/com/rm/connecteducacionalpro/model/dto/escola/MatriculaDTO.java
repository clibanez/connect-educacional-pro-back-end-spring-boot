package com.rm.connecteducacionalpro.model.dto.escola;

import com.rm.connecteducacionalpro.model.escola.Matricula;
import com.rm.connecteducacionalpro.model.escola.Turma;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatriculaDTO {

    private Long id;
    private Student studentId;
    private Turma turmaId;
    private LocalDate dataMatricula;

    private BigDecimal valorMatricula;

    public MatriculaDTO(Matricula obj) {
        this.id = obj.getId();
        this.studentId = obj.getStudent();
        this.turmaId = obj.getTurma();
        this.dataMatricula = obj.getDataMatricula();
        this.valorMatricula = obj.getValorMatricula();
    }
}