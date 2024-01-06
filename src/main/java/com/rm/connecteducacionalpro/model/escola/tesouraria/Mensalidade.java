package com.rm.connecteducacionalpro.model.escola.tesouraria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.connecteducacionalpro.model.dto.escola.tesouraria.MensalidadeDTO;
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
@Table(name = "mensalidades")
public class Mensalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagePagamento;

    private String description;

    private BigDecimal valorMensalidade;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDoPagamento;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    public Mensalidade(MensalidadeDTO obj) {
        this.id = obj.getId();
        this.imagePagamento = obj.getImagePagamento ();
        this.valorMensalidade = obj.getValorMensalidade();
        this.dataDoPagamento = obj.getDataDoPagamento();
        this.description = obj.getDescription();

    }



}
