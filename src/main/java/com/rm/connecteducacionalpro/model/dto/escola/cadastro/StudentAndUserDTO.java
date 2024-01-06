package com.rm.connecteducacionalpro.model.dto.escola.cadastro;

import com.rm.connecteducacionalpro.model.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentAndUserDTO {

    @Valid
    private UserDTO userDTO;

    @Valid
    private StudentDTO studentDTO;
}
