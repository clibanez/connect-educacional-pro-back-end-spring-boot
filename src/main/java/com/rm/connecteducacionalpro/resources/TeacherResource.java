package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.model.dto.escola.cadastro.TeacherAndUserDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.TeacherDTO;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import com.rm.connecteducacionalpro.services.TeacherService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping(value = "/teachers")
public class TeacherResource {

    public final TeacherService teacherService;



    @GetMapping(value = "/{id}")
    public ResponseEntity<TeacherDTO> findById(@PathVariable Long id) {
        Teacher teacher = teacherService.findById(id);
        return ResponseEntity.ok().body(new TeacherDTO(teacher));
    }

    @GetMapping(value = "/images/{imageName}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String imageName) {
        try {
            byte[] imageBytes = teacherService.imageTeacher(imageName);
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

    @GetMapping
    public ResponseEntity<List<TeacherDTO>> findAll() {
        List<Teacher > list = teacherService.findAll();
        List<TeacherDTO> listDTO = list.stream().map(obj -> new TeacherDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping
//@PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<TeacherDTO> create(@Validated TeacherAndUserDTO teacherAndUserDTO,
                                                        @RequestPart("file") MultipartFile file) {
        Teacher teacher = teacherService.create(
                teacherAndUserDTO.getUserDTO(),
                teacherAndUserDTO.getTeacherDTO(),
                file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(teacher.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        try {
            // Delete the employee using the service method
            teacherService.deleteteacher(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    public String imagePath() {
//        School defesaCivil = UserAutenticantion.authenticated().getSchool();
//        if (defesaCivil == null) {
//            throw new AuthorizationServiceException("Acesso negado!");
//        }
//        String imagePath = "./defesa civil imagens/" + defesaCivil.getName() + "/Foto usuarios/";
//        File directory = new File(imagePath);
//        if (!directory.exists()) {
//            // Se o diretório não existir, cria-o
//            if (directory.mkdirs()) {
//            }
//        }
//        return imagePath;
//    }




    @PutMapping("/{userId}")
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR)")
    public ResponseEntity<TeacherDTO> update(
            @PathVariable Long userId,
            @Validated TeacherAndUserDTO teacherAndUserDTO,
            @Valid @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Teacher updatedTeacher = teacherService.update(userId, teacherAndUserDTO.getUserDTO(),
                 file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(updatedTeacher.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // esse updat esta completo
//    public ResponseEntity<TeacherDTO> update(
//            @PathVariable Long userId,
//            @Validated TeacherAndUserDTO teacherAndUserDTO,
//            @Valid @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
//        Teacher updatedTeacher = teacherService.update(userId, teacherAndUserDTO.getUserDTO(),
//                file);
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
//                .buildAndExpand(updatedTeacher.getId()).toUri();
//        return ResponseEntity.created(uri).build();
//    }
}

