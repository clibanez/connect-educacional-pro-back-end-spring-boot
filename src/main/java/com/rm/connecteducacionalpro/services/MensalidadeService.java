package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.dto.escola.tesouraria.MensalidadeDTO;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Mensalidade;
import com.rm.connecteducacionalpro.repositories.MensalidadeRepository;
import com.rm.connecteducacionalpro.repositories.StudentRepository;
import com.rm.connecteducacionalpro.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class MensalidadeService {

    @Autowired
    private MensalidadeRepository mensalidadeRepository;

    @Autowired
    private StudentRepository studentRepository;


    public byte[] getStudentImage(String imagePagamento) throws IOException {
        String imagePath = this.imagePath();
        File imageFile = new File(imagePath + imagePagamento);
        if (imagePagamento != null && imagePagamento.trim().length() > 0) {
            return Files.readAllBytes(imageFile.toPath());
        }

        return null;
    }


    public byte[] imageMensalidade(String image) throws IOException {
        String imagePath = this.imagePath();
        File imagemArquivo = new File(imagePath + image);
        if (image != null || image.trim().length() > 0) {
            return Files.readAllBytes(imagemArquivo.toPath());
        }
        return null;
    }


    public Mensalidade findById(Long id) {
        Optional<Mensalidade> mensalidade = mensalidadeRepository.findById(id);
        return mensalidade.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Mensalidade> findAll(Long studentId) {

        Optional<Student> student = studentRepository.findById(studentId);

        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        List<Mensalidade> mensalidades = mensalidadeRepository.findByStudent(student);
        return mensalidades;
    }


    //O.B.S junta o username com employeename lembrado que tem que mudar o nome da imagem salva
    public Mensalidade create(Long studentId, MensalidadeDTO mensalidadeDTO, MultipartFile file) {
        Optional<Student> student = studentRepository.findById(studentId);

        this.createImage(student, mensalidadeDTO, file);

        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        Mensalidade mensalidade = new Mensalidade(mensalidadeDTO);
        mensalidade.setId(null);
        mensalidade.setStudent(student.get());
       return mensalidadeRepository.save(mensalidade);

    }

    public String imagePath() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        String imagePath = "./connect educacional pro/" + school.getName() + "/Foto mensalidades/";
        File directory = new File(imagePath);
        if (!directory.exists()) {
            // Se o diretório não existir, cria-o
            if (directory.mkdirs()) {
            }
        }
        return imagePath;
    }


    public void createImage(Optional<Student> student, MensalidadeDTO mensalidadeDTO, MultipartFile file) {
        try {
            String imagePath = this.imagePath();
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path caminho = Paths.get(imagePath + student.get().getName() + file.getOriginalFilename());
                Files.write(caminho, bytes);
                mensalidadeDTO.setImagePagamento(student.get().getName() + file.getOriginalFilename());
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    @Transactional
    public void deleteMensalidade(Long id) throws IOException {
        String imagePath = this.imagePath();

        // Check if the employee exists before deleting
        Mensalidade mensalidade = findById(id);

        if (mensalidade  != null) {
            // Delete the associated image, if it exists
            if (mensalidade.getImagePagamento() != null) {
                Path caminho = Paths.get(imagePath + mensalidade.getImagePagamento());
                Files.deleteIfExists(caminho);
            }

            mensalidadeRepository.deleteById(id);
        } else {
            if (mensalidade == null) {
            } else {
                System.out.println("Administrator não pode ser deletado!.");
            }
        }
    }

public Mensalidade update(Long id, MensalidadeDTO mensalidadeDTO, MultipartFile file) throws IOException {
    Optional<Mensalidade> optionalMensalidade = mensalidadeRepository.findById(id);

    if (optionalMensalidade.isEmpty()) {
        throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
    }

    Mensalidade mensalidade = optionalMensalidade.get();

    // Atualiza os campos da mensalidade com os valores do DTO, se eles não forem nulos
    if (mensalidadeDTO.getValorMensalidade() != null) {
        mensalidade.setValorMensalidade(mensalidadeDTO.getValorMensalidade());
    }

    if (mensalidadeDTO.getDataDoPagamento() != null) {
        mensalidade.setDataDoPagamento(mensalidadeDTO.getDataDoPagamento());
    }

    // Atualiza a imagem de pagamento apenas se o arquivo não for nulo e não estiver vazio
    if (file != null && !file.isEmpty()) {
        Optional<Student> optionalStudent = studentRepository.findById(mensalidade.getStudent().getId());

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            // Deleta a imagem antiga, se existir
            if (mensalidade.getImagePagamento() != null) {
                Path caminho = Paths.get(imagePath() + mensalidade.getImagePagamento());
                Files.deleteIfExists(caminho);
            }

            // Cria a nova imagem e atualiza o nome no objeto Mensalidade
            createImage(Optional.of(student), mensalidadeDTO, file);
            mensalidade.setImagePagamento(mensalidadeDTO.getImagePagamento());
        }
    }

    // Salva as atualizações no repositório
    return mensalidadeRepository.save(mensalidade);
}


}
