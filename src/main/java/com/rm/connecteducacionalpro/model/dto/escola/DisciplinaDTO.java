package com.rm.connecteducacionalpro.model.dto.escola;
import com.rm.connecteducacionalpro.model.escola.Disciplina;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DisciplinaDTO {

    private Long id;
    private String name;



    public DisciplinaDTO(Disciplina obj) {
        this.id = obj.getId();
        this.name = obj.getName();

    }
}
