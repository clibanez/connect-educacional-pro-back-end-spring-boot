package com.rm.connecteducacionalpro.model.dto;


import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.enuns.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank(message = "O campo USUÁRIO é requerido")
    @NotNull
    @NotEmpty
    private String username;

//    @NotBlank(message = "O campo EMAIL é requerido")
    @Email(message = "O campo EMAIL deve ser um endereço de email válido")
    private String email;

//    @NotBlank(message = "O campo Senha é requerido")
//    @Size(min = 6, message = "O campo SENHA deve ter no mínimo 6 caracteres")
    private String password;

    private RoleEnum role;

    private String codigoRecuperacaoSenha;

    private Date dataEnvioCodigo;

    private School school;


    // Getters e setters

    // Outros métodos, se necessário

//    public User addAdmin(UserDTO userDTO) {
//        return new User(
//                this.id,
//                this.email,
//                this.username,
//                GeneralUtilies.encode(this.password),
//                RoleEnum.ROLE_DIRECTOR,
//                this.school,
//                this.codigoRecuperacaoSenha,
//                this.dataEnvioCodigo
//        );
//    }
//
//    public User addDutyOfficer(UserDTO userDTO) {
//        return new User(
//                null,
//                this.email,
//                this.username,
//                GeneralUtilies.encode(this.password),
//                RoleEnum.ROLE_SECRETARY,
//                this.school,
//                this.codigoRecuperacaoSenha,
//                this.dataEnvioCodigo
//        );
//    }
}