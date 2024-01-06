package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.model.dto.escola.cadastro.StudentDTO;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.StudentAndUserDTO;
import com.rm.connecteducacionalpro.repositories.StudentRepository;
import com.rm.connecteducacionalpro.services.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping(value = "/students")
public class StudentResource {
    public final StudentService studentService;

    public final StudentRepository studentRepository;

     //Lista todos os estudantes de uma turma
    @GetMapping("/byTurma/{turmaId}")
    public List<Student> getStudentsByTurmaId(@PathVariable Long turmaId) {
        return studentService.getAllStudentsByTurmaId(turmaId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StudentDTO> findById(@PathVariable Long id) {
        Student student = studentService.findById(id);
        return ResponseEntity.ok().body(new StudentDTO(student));
    }

    @GetMapping(value = "/images/{imageName}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String imageName) {
        try {
            byte[] imageBytes = studentService.getStudentImage(imageName);
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
    public ResponseEntity<List<StudentDTO>> findAll() {
        List<Student> list = studentService.findAll();
        List<StudentDTO> listDTO = list.stream().map(obj -> new StudentDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping
//@PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<StudentDTO> create(@Validated StudentAndUserDTO studentAndUserDTO,
                                             @RequestPart("file") MultipartFile file) {
        Student student = studentService.create(
                studentAndUserDTO.getUserDTO(),
                studentAndUserDTO.getStudentDTO(),

                file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(student.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> softDeleteStudent(@PathVariable Long id) {
        studentService.softDeleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR)")
    public ResponseEntity<StudentDTO> update(
            @PathVariable Long userId,
            @Validated StudentAndUserDTO studentAndUserDTO,
            @Valid @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Student updatedStudent = studentService.update(userId, studentAndUserDTO.getUserDTO(),
                file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(updatedStudent.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }


}

