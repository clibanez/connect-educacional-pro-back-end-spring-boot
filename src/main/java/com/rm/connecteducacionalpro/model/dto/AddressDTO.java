package com.rm.connecteducacionalpro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rm.connecteducacionalpro.model.Address;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private String zipCode;
    private String country;
    @NotBlank(message = "O campo ESTADO é requerido")
    @Size(min = 2, max = 2, message = "O campo ESTADO deve ter no mínimo 10 caracteres")
    private String state;
    @NotBlank(message = "O campo CIDADE é requerido")
    @NotNull
    @NotEmpty
    private String city;
    @NotBlank(message = "O campo BAIRRO é requerido")
    private String district;
    @NotBlank(message = "O campo RUA é requerido")
    private String street;
    private int houseNumber;
    private String complement;

    public AddressDTO(Address address) {
        this.zipCode = address.getZipCode();
        this.country = address.getCountry();
        this.state = address.getState();
        this.city = address.getCity();
        this.district = address.getDistrict();
        this.street = address.getStreet();
        this.houseNumber = address.getHouseNumber();
        this.complement = address.getComplement();

    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class StudentDTO {

        private Long id;
        private String name;

        private String imageName;

        private Long code;

        @JsonIgnoreProperties({"student"})
        private User user;

    //    private List<SchoolClass> schoolClass = new ArrayList<>(); // IDs das turmas que o aluno está matriculado

        public StudentDTO(Student obj) {
            this.id = obj.getId();
            this.name = obj.getName();
            this.imageName = obj.getImageName();
            this.user = obj.getUser();
            this.code = obj.getCode();
        }
    }
}
