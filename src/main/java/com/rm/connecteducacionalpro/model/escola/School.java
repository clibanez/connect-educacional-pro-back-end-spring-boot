package com.rm.connecteducacionalpro.model.escola;


import com.rm.connecteducacionalpro.model.Address;
import com.rm.connecteducacionalpro.model.dto.escola.SchoolDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "school")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false, unique = true)
    private String imageName;

    @Column(nullable = false, unique = true)
    private String name;

    @CNPJ
    private String cnpj;

    private String telephone;

    @Embedded
    private Address address;

    private LocalDateTime date = LocalDateTime.now();


    public School(SchoolDTO schoolDTO){
        this.name = schoolDTO.getName();
        this.imageName = schoolDTO.getImageName();
        this.cnpj = schoolDTO.getCnpj();
        this.telephone = schoolDTO.getTelephone();
        this.address = new Address(schoolDTO.getAddressDTO());
    }
}

