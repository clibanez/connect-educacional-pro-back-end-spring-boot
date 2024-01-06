package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.model.dto.AddressDTO;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.model.dto.escola.MatriculaDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.StudentAndUserDTO;
import com.rm.connecteducacionalpro.model.dto.escola.tesouraria.SaidaDTO;
import com.rm.connecteducacionalpro.model.escola.Matricula;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Saida;
import com.rm.connecteducacionalpro.repositories.SaidaRepository;
import com.rm.connecteducacionalpro.services.MatriculaService;
import com.rm.connecteducacionalpro.services.SaidaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/matriculas")
public class MatriculaResource {

    @Autowired
    private MatriculaService matriculaService;


    @PostMapping
    public ResponseEntity<Matricula> criarMatricula(
            @Validated StudentAndUserDTO studentAndUserDTO,
            @RequestPart("file") MultipartFile file,
            MatriculaDTO matriculaDTO
    ) {
        Matricula matriculaCriada = matriculaService.criarMatricula(
                studentAndUserDTO.getUserDTO(),
                studentAndUserDTO.getStudentDTO(),
                file, matriculaDTO);
        return new ResponseEntity<>(matriculaCriada, HttpStatus.CREATED);
    }

//    @PostMapping
//    public ResponseEntity<Matricula> criarMatricula(@RequestBody MatriculaDTO matriculaDTO) {
//        Matricula matriculaCriada = matriculaService.criarMatricula(matriculaDTO);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

}

