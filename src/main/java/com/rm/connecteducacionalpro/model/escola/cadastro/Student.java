package com.rm.connecteducacionalpro.model.escola.cadastro;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.StudentDTO;
import com.rm.connecteducacionalpro.model.escola.Matricula;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageName;

    private Long code;

    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "active", columnDefinition = "boolean default true")
    private boolean active = true;

    @JsonBackReference
    @OneToMany(mappedBy = "student")
    private List<Matricula> matriculas = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;


//    @OneToMany(mappedBy = "student")
//    private List<Matricula> matriculas = new ArrayList<>();

    public Student(StudentDTO obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.imageName = obj.getImageName();
        this.user = obj.getUser();
        this.code = obj.getCode();
    }

    public void generateProtocolNumber() {
        // Formate a data atual como uma string (sem separadores, para garantir unicidade)
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // Gere um número aleatório de 3 dígitos
        Random random = new Random();
        int randomDigits = random.nextInt(900) + 100;

        // Combine a data formatada e os dígitos aleatórios para criar o número do protocolo
        String protocolNumberString = formattedDate + randomDigits;
        this.code = Long.parseLong(protocolNumberString);
    }

    // Inside the Student class
    @Column(name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted = false;
}
