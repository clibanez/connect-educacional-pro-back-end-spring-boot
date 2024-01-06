package com.rm.connecteducacionalpro.model.dto.escola;


import com.rm.connecteducacionalpro.model.dto.AddressDTO;
import com.rm.connecteducacionalpro.model.escola.School;
import jakarta.validation.Valid;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchoolDTO {

    private String name;

    private String imageName;

    private String cnpj;

    private String telephone;
    @Valid
    private AddressDTO addressDTO;

    public SchoolDTO(School school){
        this.name = school.getName();
        this.imageName = school.getImageName();
        this.cnpj = school.getCnpj();
        this.telephone = school.getTelephone();
        this.addressDTO = new AddressDTO(school.getAddress());

    }
}
