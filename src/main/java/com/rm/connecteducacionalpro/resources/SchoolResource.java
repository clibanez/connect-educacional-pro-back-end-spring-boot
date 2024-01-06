package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.dto.SchoolAndUserDTOAndEmployee;
import com.rm.connecteducacionalpro.model.dto.escola.SchoolDTO;
import com.rm.connecteducacionalpro.services.SchoolService;
import lombok.AllArgsConstructor;
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
import java.util.stream.Collectors;


@AllArgsConstructor
@RestController
@RequestMapping(value = "/school")
public class SchoolResource {

    @Autowired
    private SchoolService schoolService;

    @GetMapping(value = "/image/{imageName}")
    public ResponseEntity<byte[]> getSchoolImage(@PathVariable String imageName) {
        try {
            byte[] imageBytes = schoolService.getEmployeeImage(imageName);
            if (imageBytes != null) {
                return ResponseEntity.ok().body(imageBytes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            // Tratar exceções de leitura de arquivo
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping() //O.B.S fazer um tratamento de erro para pegar a mensagem do @valid
    public ResponseEntity<String> create(
            @Validated SchoolAndUserDTOAndEmployee schoolAndUserDTOAndEmployee,
            @RequestPart("fileSchool") MultipartFile fileSchool,
            @RequestPart("fileEmployee") MultipartFile fileEmployee
    ) {
        try {
            // Lógica para salvar a escola, usuário e funcionário
            School school = schoolService.create(
                    schoolAndUserDTOAndEmployee.getSchoolDTO(),
                    schoolAndUserDTOAndEmployee.getUserDTO(),
                    schoolAndUserDTOAndEmployee.getEmployeeDTO(),
                    fileEmployee,
                    fileSchool
            );

            // Construir a URI de resposta com base no ID da escola criada
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(school.getId())
                    .toUri();

            // Retornar uma resposta de sucesso com a URI de localização
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            // Logar o erro ou retornar uma resposta de erro apropriada
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro durante a criação da escola.");
        }
    }




    @GetMapping()
    public ResponseEntity<SchoolDTO> findAll() {
        School schools = schoolService.findAllAutentication();
        return ResponseEntity.ok().body(new SchoolDTO(schools));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<SchoolDTO>> findAllDefesaCivil() {
        List<School> list = schoolService.findAll();
        List<SchoolDTO> listDTO = list.stream().map(obj -> new SchoolDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }


    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllDefesaCivilNames() {
        List<String> schoolNames = schoolService.getAllDefesaCivilNames();
        return ResponseEntity.ok(schoolNames);
    }



}


