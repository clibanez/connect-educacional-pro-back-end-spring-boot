package com.rm.connecteducacionalpro.model.dto.escola.tesouraria;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Saida;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaidaDTO {

    private Long id;

    private String imagePagamento;

    private String description;

    private BigDecimal valorSalario;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDoPagamento;

    @JsonIgnore
    private Teacher teacher;

    @JsonIgnore
    private Employee employee;


    public SaidaDTO(Saida obj) {
        this.id = obj.getId();
        this.imagePagamento = obj.getImagePagamento();
        this.valorSalario = obj.getValorSalario();
        this.dataDoPagamento = obj.getDataDoPagamento();
        this.description = obj.getDescription();

    }

}

