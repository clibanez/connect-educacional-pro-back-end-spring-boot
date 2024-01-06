package com.rm.connecteducacionalpro.model.dto;

import com.rm.connecteducacionalpro.model.dto.escola.SchoolDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.EmployeeDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchoolAndUserDTOAndEmployee {

    @Valid
    private SchoolDTO schoolDTO;

    @Valid
    private UserDTO userDTO;

    //    @Valid
    private EmployeeDTO employeeDTO;
}
