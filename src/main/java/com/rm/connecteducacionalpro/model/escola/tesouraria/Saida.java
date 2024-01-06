package com.rm.connecteducacionalpro.model.escola.tesouraria;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.connecteducacionalpro.model.dto.escola.tesouraria.MensalidadeDTO;
import com.rm.connecteducacionalpro.model.dto.escola.tesouraria.SaidaDTO;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
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
@Table(name = "cashoutflows")
public class Saida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagePagamento;

    private String description;

    private BigDecimal valorSalario;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDoPagamento;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    public Saida(SaidaDTO obj) {
        this.id = obj.getId();
        this.imagePagamento = obj.getImagePagamento ();
        this.valorSalario = obj.getValorSalario();
        this.dataDoPagamento = obj.getDataDoPagamento();
        this.description = obj.getDescription();

    }



}