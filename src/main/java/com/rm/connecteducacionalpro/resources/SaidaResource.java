package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.model.dto.escola.tesouraria.SaidaDTO;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Saida;
import com.rm.connecteducacionalpro.repositories.SaidaRepository;
import com.rm.connecteducacionalpro.services.SaidaService;
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
@RequestMapping(value = "/saidas")
public class SaidaResource {

    @Autowired
    public SaidaService saidaService;

    @Autowired
    public SaidaRepository saidaRepository;

    @GetMapping(value = "findbyid/{id}")
    public ResponseEntity<SaidaDTO> findById(@PathVariable Long id) {
        Saida saida = saidaService.findById(id);
        return ResponseEntity.ok().body(new SaidaDTO(saida));
    }

    @GetMapping(value = "/images/{imagePagamento}")
    public ResponseEntity<byte[]> getTeacherImage(@PathVariable String imagePagamento) {
        try {
            byte[] imageBytes = saidaService.getTeacherImage(imagePagamento);
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
    public List<Saida> listaDizmoDeUmMembro(@PathVariable Long id) {
        return saidaService.findAll(id);
    }

    @PostMapping(value = "/{teacherId}")
//@PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<SaidaDTO> create(@PathVariable Long teacherId, @Validated SaidaDTO SaidaDTO,
                                                 @RequestPart("file") MultipartFile file) {
        Saida saida = saidaService.create(teacherId,SaidaDTO, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saida.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> softDeleteSaida(@PathVariable Long id) {
        try {
            saidaService.deleteSaida(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR)")
    public ResponseEntity<SaidaDTO> update(
            @PathVariable Long id,
            SaidaDTO SaidaDTO,
            @Valid @RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
        Saida Saida = saidaService.update(id, SaidaDTO, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(Saida.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }


//     Employee
    @GetMapping(value = "/image-payment-employee/{imagePaymentEmployee}")
    public ResponseEntity<byte[]> getImagePaymentEmployee(@PathVariable String imagePaymentEmployee) {
        try {
            byte[] imageBytes = saidaService.getImagePaymentEmployee(imagePaymentEmployee);
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

    @GetMapping(value = "payment-findbyid-employee/{paymentEmployeeId}")
    public ResponseEntity<SaidaDTO> findByIdPaymentEmployee(@PathVariable Long paymentEmployeeId) {
        Saida saida = saidaService.findById(paymentEmployeeId);
        return ResponseEntity.ok().body(new SaidaDTO(saida));
    }



    @GetMapping("payment-findall-employee/{employeeId}")
    public List<Saida> findAllPaymentEmployee(@PathVariable Long employeeId) {
        return saidaService.findAllPaymentEmployee(employeeId);
    }

    @PostMapping(value = "create-payment-employee/{employeeId}")
//@PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR')")
    public ResponseEntity<SaidaDTO> createPaymentEmployee(@PathVariable Long employeeId, @Validated SaidaDTO SaidaDTO,
                                           @RequestPart("file") MultipartFile file) {
        Saida Saida = saidaService.createPaymentEmployee(employeeId,SaidaDTO, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(Saida.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/update-payment-employee/{paymentEmployeeId}")
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'DIRECTOR)")
    public ResponseEntity<SaidaDTO> updatePaymentEmployee(
            @PathVariable Long paymentEmployeeId,
            SaidaDTO saidaDTO,
            @Valid @RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
        System.out.println("----> " + paymentEmployeeId);
        System.out.println("----> " + saidaDTO.getEmployee());
        System.out.println("----> " + saidaDTO.getDescription());
        Saida Saida = saidaService.updatePaymentEmployee(paymentEmployeeId, saidaDTO, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(Saida.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "delete-payment-employee/{id}")
    public ResponseEntity<Void> softDeletePaymentEmployee(@PathVariable Long id) {
        try {
            saidaService.deletePaymentEmployee(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }


}

