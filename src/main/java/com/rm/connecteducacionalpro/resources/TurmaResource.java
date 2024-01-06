package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.model.dto.escola.TurmaDTO;
import com.rm.connecteducacionalpro.model.escola.Turma;
import com.rm.connecteducacionalpro.services.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/turmas")
public class TurmaResource {


    @Autowired
    private TurmaService turmaService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<TurmaDTO> findById(@PathVariable Long id) {
        Turma turma = turmaService.findById(id);
        return ResponseEntity.ok().body(new TurmaDTO(turma));
    }

    @GetMapping
    public ResponseEntity<List<Turma>> findAllByTeacherSchoolId() {
        List<Turma> turmas = turmaService.findAllByTeacherSchoolId();
        return new ResponseEntity<>(turmas, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Turma> criarTurmaComRelacionamentos(@RequestBody TurmaDTO turmaDTO) {
        Turma turmaCriada = turmaService.criarTurmaComRelacionamentos(turmaDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
