package com.rm.connecteducacionalpro.model.escola.cadastro;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.SecretaryDTO;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "secretarys")
public class Secretary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @Column(nullable = false, unique = true)
    private String imageName;

    @Column(nullable = false)
    private String name;

//    @Column(nullable = false)
//    private String documentId;
//
//
//    private String telephone;

//    @Embedded
//    private Address address;

//    private LocalDateTime date = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;


    public Secretary(SecretaryDTO obj) {
        this.id = obj.getId();
        this.imageName = obj.getImageName();
        this.name = obj.getName();
//        this.telephone = obj.getTelephone();
//        this.address = new Address(obj.getAddressDTO());
        this.user = obj.getUser();
//        this.documentId = obj.getDocumentId();

    }
}

