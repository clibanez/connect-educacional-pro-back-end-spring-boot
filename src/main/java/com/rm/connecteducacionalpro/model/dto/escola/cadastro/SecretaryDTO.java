package com.rm.connecteducacionalpro.model.dto.escola.cadastro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.escola.cadastro.Secretary;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SecretaryDTO {

    //    @NotBlank(message = "O campo IMAGENAME é requerido")
    private String imageName;
    private Long id;
    //    @NotBlank(message = "O campo NOME é requerido")
    private String name;

//    @NotBlank(message = "O campo CPF é requerido")
//    private String documentId;
//
//    private String telephone;

//    @Valid
//    private AddressDTO addressDTO;

    //    @JsonIgnore
    @JsonIgnoreProperties({"secretary"})
    private User user;

    public SecretaryDTO(Secretary obj) {
        this.id = obj.getId();
        this.imageName = obj.getImageName();
        this.name = obj.getName();
//        this.telephone = obj.getTelephone();
//        this.addressDTO = new AddressDTO(obj.getAddress());
        this.user = obj.getUser();
//        this.documentId = obj.getDocumentId();
    }
}

