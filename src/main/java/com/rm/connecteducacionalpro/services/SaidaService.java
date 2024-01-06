package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.dto.escola.tesouraria.SaidaDTO;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import com.rm.connecteducacionalpro.model.escola.tesouraria.Saida;
import com.rm.connecteducacionalpro.repositories.EmployeeRepository;
import com.rm.connecteducacionalpro.repositories.SaidaRepository;
import com.rm.connecteducacionalpro.repositories.TeacherRepository;
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
public class SaidaService {
    
    @Autowired
    private SaidaRepository saidaRepository;

    @Autowired
    private TeacherRepository teacherRepository;


    @Autowired
    private EmployeeRepository employeeRepository;


    public byte[] getTeacherImage(String imagePagamento) throws IOException {
        String imagePath = this.imagePath();
        File imageFile = new File(imagePath + imagePagamento);
        if (imagePagamento != null && imagePagamento.trim().length() > 0) {
            return Files.readAllBytes(imageFile.toPath());
        }

        return null;
    }


    public byte[] imageSaida(String image) throws IOException {
        String imagePath = this.imagePath();
        File imagemArquivo = new File(imagePath + image);
        if (image != null || image.trim().length() > 0) {
            return Files.readAllBytes(imagemArquivo.toPath());
        }
        return null;
    }


    public Saida findById(Long id) {
        Optional<Saida> saida = saidaRepository.findById(id);
        return saida.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Saida> findAll(Long teacherId) {

        Optional<Teacher> teacher = teacherRepository.findById(teacherId);

        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        List<Saida> Saidas = saidaRepository.findByTeacher(teacher);
        return Saidas;
    }


    //O.B.S junta o username com employeename lembrado que tem que mudar o nome da imagem salva
    public Saida create(Long studentId, SaidaDTO saidaDTO, MultipartFile file) {
        Optional<Teacher> teacher = teacherRepository.findById(studentId);

        this.createImage(teacher, saidaDTO, file);

        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        Saida Saida = new Saida(saidaDTO);
        Saida.setId(null);
        Saida.setTeacher(teacher.get());
        return saidaRepository.save(Saida);

    }

    public String imagePath() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        String imagePath = "./connect educacional pro/" + school.getName() + "/Foto Saidas/";
        File directory = new File(imagePath);
        if (!directory.exists()) {
            // Se o diretório não existir, cria-o
            if (directory.mkdirs()) {
            }
        }
        return imagePath;
    }


    public void createImage(Optional<Teacher> teacher, SaidaDTO SaidaDTO, MultipartFile file) {
        try {
            String imagePath = this.imagePath();
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path caminho = Paths.get(imagePath + teacher.get().getName() + file.getOriginalFilename());
                Files.write(caminho, bytes);
                SaidaDTO.setImagePagamento(teacher.get().getName() + file.getOriginalFilename());
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    @Transactional
    public void deleteSaida(Long id) throws IOException {
        String imagePath = this.imagePath();

        // Check if the employee exists before deleting
        Saida Saida = findById(id);

        if (Saida  != null) {
            // Delete the associated image, if it exists
            if (Saida.getImagePagamento() != null) {
                Path caminho = Paths.get(imagePath + Saida.getImagePagamento());
                Files.deleteIfExists(caminho);
            }

            saidaRepository.deleteById(id);
        } else {
            if (Saida == null) {
            } else {
                System.out.println("Administrator não pode ser deletado!.");
            }
        }
    }

    public Saida update(Long id, SaidaDTO saidaDTO, MultipartFile file) throws IOException {
        Optional<Saida> optionalSaida = saidaRepository.findById(id);

        if (optionalSaida.isEmpty()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
        }

        Saida saida = optionalSaida.get();

        // Atualiza os campos da Saida com os valores do DTO, se eles não forem nulos
        if (saidaDTO.getValorSalario() != null) {
            saida.setValorSalario(saidaDTO.getValorSalario());
        }

        if (saidaDTO.getDataDoPagamento() != null) {
            saida.setDataDoPagamento(saidaDTO.getDataDoPagamento());
        }

        // Atualiza a imagem de pagamento apenas se o arquivo não for nulo e não estiver vazio
        if (file != null && !file.isEmpty()) {
            Optional<Teacher> optionalTeacher = teacherRepository.findById(saida.getTeacher().getId());

            if (optionalTeacher.isPresent()) {
                Teacher teacher = optionalTeacher.get();

                // Deleta a imagem antiga, se existir
                if (saida.getImagePagamento() != null) {
                    Path caminho = Paths.get(imagePath() + saida.getImagePagamento());
                    Files.deleteIfExists(caminho);
                }

                // Cria a nova imagem e atualiza o nome no objeto Saida
                createImage(Optional.of(teacher), saidaDTO, file);
                saida.setImagePagamento(saidaDTO.getImagePagamento());
            }
        }
        // Salva as atualizações no repositório
        return saidaRepository.save(saida);
    }




//    Employee

    public byte[] getImagePaymentEmployee(String imagePagamento) throws IOException {
        String imagePath = this.imagePathPaymentEmployee();
        File imageFile = new File(imagePath + imagePagamento);
        if (imagePagamento != null && imagePagamento.trim().length() > 0) {
            return Files.readAllBytes(imageFile.toPath());
        }

        return null;
    }



    public List<Saida> findAllPaymentEmployee(Long employeeId) {

        Optional<Employee> employee = employeeRepository.findById(employeeId);

        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        List<Saida> Saidas = saidaRepository.findByEmployee(employee);
        return Saidas;
    }


    //O.B.S junta o username com employeename lembrado que tem que mudar o nome da imagem salva
    public Saida createPaymentEmployee(Long employeeId, SaidaDTO saidaDTO, MultipartFile file) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        this.createImagePaymentEmployee(employee, saidaDTO, file);

        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        Saida Saida = new Saida(saidaDTO);
        Saida.setId(null);
        Saida.setEmployee(employee.get());
        return saidaRepository.save(Saida);

    }

    public String imagePathPaymentEmployee() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        String imagePath = "./connect educacional pro/" + school.getName() + "/Foto pagamentos funcionarios/";
        File directory = new File(imagePath);
        if (!directory.exists()) {
            // Se o diretório não existir, cria-o
            if (directory.mkdirs()) {
            }
        }
        return imagePath;
    }


    public void createImagePaymentEmployee(Optional<Employee> employee, SaidaDTO SaidaDTO, MultipartFile file) {
        try {
            String imagePath = this.imagePathPaymentEmployee();
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path caminho = Paths.get(imagePath + employee.get().getName() + file.getOriginalFilename());
                Files.write(caminho, bytes);
                SaidaDTO.setImagePagamento(employee.get().getName() + file.getOriginalFilename());
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public Saida updatePaymentEmployee(Long paymentEmployeeId, SaidaDTO saidaDTO, MultipartFile file) throws IOException {
        Optional<Saida> optionalSaida = saidaRepository.findById(paymentEmployeeId);

        if (optionalSaida.isEmpty()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + paymentEmployeeId);
        }

        Saida saida = optionalSaida.get();

        // Atualiza os campos da Saida com os valores do DTO, se eles não forem nulos

        if (saidaDTO.getDescription() != null) {
            saida.setDescription(saidaDTO.getDescription());
        }

        if (saidaDTO.getValorSalario() != null) {
            saida.setValorSalario(saidaDTO.getValorSalario());
        }

        if (saidaDTO.getValorSalario() != null) {
            saida.setValorSalario(saidaDTO.getValorSalario());
        }




        // Atualiza a imagem de pagamento apenas se o arquivo não for nulo e não estiver vazio
        if (file != null && !file.isEmpty()) {
            Optional<Employee> optionalEmployee = employeeRepository.findById(saida.getEmployee().getId());

            if (optionalEmployee.isPresent()) {
                Employee employee = optionalEmployee.get();

                // Deleta a imagem antiga, se existir
                if (saida.getImagePagamento() != null) {
                    Path caminho = Paths.get(imagePathPaymentEmployee() + saida.getImagePagamento());
                    Files.deleteIfExists(caminho);
                }

                // Cria a nova imagem e atualiza o nome no objeto Saida
                createImagePaymentEmployee(Optional.of(employee), saidaDTO, file);
                saida.setImagePagamento(saidaDTO.getImagePagamento());
            }
        }

        System.out.println("--->>> " +  saida.getDescription());
        // Salva as atualizações no repositório
        return saidaRepository.save(saida);
    }


    @Transactional
    public void deletePaymentEmployee(Long id) throws IOException {
        String imagePath = this.imagePathPaymentEmployee();

        // Check if the employee exists before deleting
        Saida Saida = findById(id);

        if (Saida  != null) {
            // Delete the associated image, if it exists
            if (Saida.getImagePagamento() != null) {
                Path caminho = Paths.get(imagePath + Saida.getImagePagamento());
                Files.deleteIfExists(caminho);
            }

            saidaRepository.deleteById(id);
        } else {
            if (Saida == null) {
            } else {
                System.out.println("Administrator não pode ser deletado!.");
            }
        }
    }


}

