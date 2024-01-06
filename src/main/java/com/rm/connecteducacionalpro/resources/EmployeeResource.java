package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.EmployeeAndUserDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.EmployeeDTO;
import com.rm.connecteducacionalpro.repositories.EmployeeRepository;
import com.rm.connecteducacionalpro.services.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/funcionarios")
public class EmployeeResource {

    public final EmployeeService employeeService;

    public final EmployeeRepository employeeRepository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        return ResponseEntity.ok().body(new EmployeeDTO(employee));
    }

    @GetMapping(value = "/images/{imageName}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String imageName) {
        try {
            byte[] imageBytes = employeeService.getEmployeeImage(imageName);
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
    public ResponseEntity<List<EmployeeDTO>> findAll() {
        List<Employee> list = employeeService.findAll();
        List<EmployeeDTO> listDTO = list.stream().map(obj -> new EmployeeDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<EmployeeDTO> create(@Validated EmployeeAndUserDTO employeeAndUserDTO,
                                              @RequestPart("file") MultipartFile file) {
        Employee employee = employeeService.create(employeeAndUserDTO.getUserDTO(), employeeAndUserDTO.getEmployeeDTO(), file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employee.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        try {
            // Delete the employee using the service method
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public String imagePath() {
        School defesaCivil = UserAutenticantion.authenticated().getSchool();
        if (defesaCivil == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        String imagePath = "./defesa civil imagens/" + defesaCivil.getName() + "/Foto usuarios/";
        File directory = new File(imagePath);
        if (!directory.exists()) {
            // Se o diretório não existir, cria-o
            if (directory.mkdirs()) {
            }
        }
        return imagePath;
    }

    @PutMapping("/update/{userId}")
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR)")
    public ResponseEntity<EmployeeDTO> update(
            @PathVariable Long userId,
            @Validated EmployeeAndUserDTO employeeAndUserDTO,
            @Valid @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Employee updatedEmployee = employeeService.update(userId, employeeAndUserDTO.getUserDTO(),
                employeeAndUserDTO.getEmployeeDTO(), file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(updatedEmployee.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}


