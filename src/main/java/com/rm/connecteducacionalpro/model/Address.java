package com.rm.connecteducacionalpro.model;

import com.rm.connecteducacionalpro.model.dto.AddressDTO;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String zipCode;
    private String country;
    private String state;
    private String city;
    private String district;
    private String street;
    private int houseNumber;
    private String complement;

    public Address(AddressDTO addressDTO) {
        this.zipCode = addressDTO.getZipCode();
        this.country = addressDTO.getCountry();
        this.state = addressDTO.getState();
        this.city = addressDTO.getCity();
        this.district = addressDTO.getDistrict();
        this.street = addressDTO.getStreet();
        this.houseNumber = addressDTO.getHouseNumber();
        this.complement = addressDTO.getComplement();
    }
}
