package com.rm.connecteducacionalpro.model.dto.escola.cadastro;

import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentDTO {

    private Long id;
    private String name;
    private String imageName;

    private Long code;

    private LocalDateTime date = LocalDateTime.now();

    private User user;




    public StudentDTO(Student obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.imageName = obj.getImageName();
        this.user = obj.getUser();
        this.code = obj.getCode();
    }
}

