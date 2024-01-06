package com.rm.connecteducacionalpro.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(Long id, @NotNull @NotEmpty @NotBlank String login, @NotNull @NotEmpty String password) {


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
