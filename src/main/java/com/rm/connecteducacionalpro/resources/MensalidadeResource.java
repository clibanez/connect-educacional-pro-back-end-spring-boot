package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.model.dto.escola.tesouraria.MensalidadeDTO;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Mensalidade;
import com.rm.connecteducacionalpro.repositories.MensalidadeRepository;
import com.rm.connecteducacionalpro.services.MensalidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/mensalidades")
public class MensalidadeResource {

    @Autowired
    public MensalidadeService mensalidadeService;

    @Autowired
    public MensalidadeRepository mensalidadeRepository;

    @GetMapping(value = "findbyid/{id}")
    public ResponseEntity<MensalidadeDTO> findById(@PathVariable Long id) {
        Mensalidade mensalidade = mensalidadeService.findById(id);
        return ResponseEntity.ok().body(new MensalidadeDTO(mensalidade));
    }

    @GetMapping(value = "/images/{imagePagamento}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String imagePagamento) {
        try {
            byte[] imageBytes = mensalidadeService.getStudentImage(imagePagamento);
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


    @GetMapping("/{id}")
    public List<Mensalidade> listaDizmoDeUmMembro(@PathVariable Long id) {
        return mensalidadeService.findAll(id);
    }

    @PostMapping(value = "/{studentId}")
//@PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<MensalidadeDTO> create(@PathVariable Long studentId, @Validated MensalidadeDTO mensalidadeDTO,
                                             @RequestPart("file") MultipartFile file) {
        Mensalidade mensalidade = mensalidadeService.create(studentId,mensalidadeDTO, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(mensalidade.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> softDeleteMensalidade(@PathVariable Long id) {
        try {
            mensalidadeService.deleteMensalidade(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR)")
    public ResponseEntity<MensalidadeDTO> update(
            @PathVariable Long id,
             MensalidadeDTO mensalidadeDTO,
            @Valid @RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
    Mensalidade mensalidade = mensalidadeService.update(id, mensalidadeDTO, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(mensalidade.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
