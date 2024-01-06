package com.rm.connecteducacionalpro.model.dto.escola.tesouraria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Mensalidade;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MensalidadeDTO {

    private Long id;
    private String imagePagamento;

    private String description;

    private BigDecimal valorMensalidade;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDoPagamento;

    @JsonIgnore
    private Student student;


    public MensalidadeDTO(Mensalidade obj) {
        this.id = obj.getId();
        this.imagePagamento = obj.getImagePagamento ();
        this.valorMensalidade = obj.getValorMensalidade();
        this.dataDoPagamento = obj.getDataDoPagamento();
        this.description = obj.getDescription();

    }
}
