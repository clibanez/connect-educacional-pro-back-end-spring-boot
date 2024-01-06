package com.rm.connecteducacionalpro.model.dto.escola.cadastro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rm.connecteducacionalpro.model.dto.AddressDTO;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeDTO {

//    @NotBlank(message = "O campo IMAGENAME é requerido")
    private String imageName;
    private Long id;
    //    @NotBlank(message = "O campo NOME é requerido")
    private String name;

    @NotBlank(message = "O campo CPF é requerido")
    private String documentId;

    @NotBlank(message = "O campo OFICIO é requerido")
    private String office;
    @NotBlank(message = "O campo FUNÇÃO é requerido")
    private String employeeFunction;

    @NotBlank(message = "O campo FUNÇÃO é requerido")
    private String telephone;


    @Valid
    private AddressDTO addressDTO;

//    @JsonIgnore
    @JsonIgnoreProperties({"employee"})
    private User user;

    public EmployeeDTO(Employee obj) {
        this.id = obj.getId();
        this.imageName = obj.getImageName();
        this.name = obj.getName();
        this.office = obj.getOffice();
        this.employeeFunction = obj.getEmployeeFunction();
        this.telephone = obj.getTelephone();
        this.addressDTO = new AddressDTO(obj.getAddress());
        this.user = obj.getUser();
        this.documentId = obj.getDocumentId();
    }
}

