package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.model.dto.escola.cadastro.SecretaryAndUserDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.SecretaryDTO;
import com.rm.connecteducacionalpro.model.escola.cadastro.Secretary;
import com.rm.connecteducacionalpro.services.SecretaryService;
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
@RequestMapping(value = "/secretarys")
public class SecretaryResource {

    public final SecretaryService secretaryService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<SecretaryDTO> findById(@PathVariable Long id) {
        Secretary secretary = secretaryService.findById(id);
        return ResponseEntity.ok().body(new SecretaryDTO(secretary));
    }

    @GetMapping(value = "/images/{imageName}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String imageName) {
        try {
            byte[] imageBytes = secretaryService.imageSecretary(imageName);
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
    public ResponseEntity<List<SecretaryDTO>> findAll() {
        List<Secretary> list = secretaryService.findAll();
        List<SecretaryDTO> listDTO = list.stream().map(obj -> new SecretaryDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping
//@PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<SecretaryDTO> create(@Validated SecretaryAndUserDTO secretaryAndUserDTO,
                                                        @RequestPart("file") MultipartFile file) {
        Secretary secretary = secretaryService.create(
                secretaryAndUserDTO.getUserDTO(),
                secretaryAndUserDTO.getSecretaryDTO(),
                file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(secretary.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        try {
            // Delete the employee using the service method
            secretaryService.deleteSecretary(id);
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
    public ResponseEntity<SecretaryDTO> update(
            @PathVariable Long userId,
            @Validated SecretaryAndUserDTO SecretaryAndUserDTO,
            @Valid @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Secretary updatedSecretary = secretaryService.update(userId, SecretaryAndUserDTO.getUserDTO(),
                 file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(updatedSecretary.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // esse updat esta completo
//    public ResponseEntity<SecretaryDTO> update(
//            @PathVariable Long userId,
//            @Validated SecretaryAndUserDTO SecretaryAndUserDTO,
//            @Valid @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
//        Secretary updatedSecretary = SecretaryService.update(userId, SecretaryAndUserDTO.getUserDTO(),
//                file);
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
//                .buildAndExpand(updatedSecretary.getId()).toUri();
//        return ResponseEntity.created(uri).build();
//    }
}

