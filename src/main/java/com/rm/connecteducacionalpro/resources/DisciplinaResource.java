package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.model.dto.escola.DisciplinaDTO;
import com.rm.connecteducacionalpro.model.escola.Disciplina;
import com.rm.connecteducacionalpro.services.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaResource {
    @Autowired
    private  DisciplinaService disciplinaService;


    @GetMapping
    public ResponseEntity<List<Disciplina>> listarDisciplinas() {
        List<Disciplina> disciplinas = disciplinaService.getAllDisciplinas();
        return new ResponseEntity<List<Disciplina>>(disciplinas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disciplina> obterDisciplinaPorId(@PathVariable Long id) {
        Optional<Disciplina> disciplina = disciplinaService.getDisciplinaById(id);
        return disciplina.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<DisciplinaDTO> criarDisciplina(@RequestBody DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = disciplinaService.criarDisciplina(disciplinaDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(disciplina.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disciplina> atualizarDisciplina(@PathVariable Long id,
                                                          @RequestBody DisciplinaDTO disciplinaDTO) {
        Disciplina disciplinaAtualizada = disciplinaService.atualizarDisciplina(id, disciplinaDTO);

        if (disciplinaAtualizada != null) {
            return new ResponseEntity<>(disciplinaAtualizada, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDisciplina(@PathVariable Long id) {
        disciplinaService.deletarDisciplina(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
