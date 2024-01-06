package com.rm.connecteducacionalpro.model.escola.cadastro;

import com.rm.connecteducacionalpro.model.Address;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.EmployeeDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @Column(nullable = false, unique = true)
    private String imageName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String documentId;

    @Column(nullable = false)
    private String office;

    @Column(nullable = false)
    private String employeeFunction;

    @NotBlank(message = "O campo FUNÇÃO é requerido")
    private String telephone;

    @Embedded
    private Address address;

//    private LocalDateTime date = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public Employee(EmployeeDTO obj) {
        this.id = obj.getId();
        this.imageName = obj.getImageName();
        this.name = obj.getName();
        this.office = obj.getOffice();
        this.employeeFunction = obj.getEmployeeFunction();
        this.telephone = obj.getTelephone();
        this.address = new Address(obj.getAddressDTO());
        this.user = obj.getUser();
        this.documentId = obj.getDocumentId();

    }
}

