package com.rm.connecteducacionalpro.model.escola;


import com.rm.connecteducacionalpro.model.Address;
import com.rm.connecteducacionalpro.model.dto.escola.DisciplinaDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.EmployeeDTO;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "disciplina")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;


    public Disciplina(DisciplinaDTO obj) {
        this.id = obj.getId();
        this.name = obj.getName();

    }

}
