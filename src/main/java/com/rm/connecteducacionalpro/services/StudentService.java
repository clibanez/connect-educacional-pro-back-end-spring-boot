package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.*;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.StudentDTO;
import com.rm.connecteducacionalpro.model.enuns.RoleEnum;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.repositories.StudentRepository;
import com.rm.connecteducacionalpro.repositories.UserRepository;
import com.rm.connecteducacionalpro.services.exception.DataIntegrityViolationException;
import com.rm.connecteducacionalpro.services.exception.ObjectNotFoundException;
import com.rm.connecteducacionalpro.util.GeneralUtilies;
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
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public UserRepository userRepository;

    //Lista todos os estudantes de uma turma
    public List<Student> getAllStudentsByTurmaId(Long turmaId) {
        // Assuming StudentRepository has a method to fetch students by turmaId
        return studentRepository.findAllByMatriculas_Turma_Id(turmaId);
    }
    public byte[] getStudentImage(String imageName) throws IOException {
        String imagePath = this.imagePath();
        File imageFile = new File(imagePath + imageName);
        if (imageName != null && imageName.trim().length() > 0) {
            return Files.readAllBytes(imageFile.toPath());
        }

        return null;
    }


    public byte[] imageAmployee(String image) throws IOException {
        String imagePath = this.imagePath();
        File imagemArquivo = new File(imagePath + image);
        if (image != null || image.trim().length() > 0) {
            return Files.readAllBytes(imagemArquivo.toPath());
        }
        return null;
    }


    // Inside StudentService
    public Student findById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.filter(s -> !s.isDeleted())
                .orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Student> findAll() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        List<Student> students = studentRepository.findByUserSchoolAndDeletedFalse(school);
        return students.stream()
                .filter(s -> !s.isDeleted())
                .collect(Collectors.toList());
    }


    //O.B.S junta o username com employeename lembrado que tem que mudar o nome da imagem salva
    public Student create(UserDTO userDTO, StudentDTO studentDTO, MultipartFile file) {
        this.validEmail(userDTO);

        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        User user = new User(userDTO);
        user.setId(null);
        user.setSchool(school);
        user.setPassword(GeneralUtilies.encode("123456"));
        user.setRole(RoleEnum.ROLE_STUDENT);
        User userId = userRepository.save(user);


        Student student = new Student();
        this.createImage(userDTO, student, file);
        student.setId(null);
        student.setName(userId.getUsername());
        student.setUser(userId);
        student.generateProtocolNumber();

        // Save employee
        return studentRepository.save(student);
    }

    public String imagePath() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        String imagePath = "./connect educacional pro/" + school.getName() + "/Foto estudantes/";
        File directory = new File(imagePath);
        if (!directory.exists()) {
            // Se o diretório não existir, cria-o
            if (directory.mkdirs()) {
            }
        }
        return imagePath;
    }

    public void createImage(UserDTO userDTO, Student student, MultipartFile file) {
        try {
            String imagePath = this.imagePath();
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path caminho = Paths.get(imagePath + userDTO.getUsername() + file.getOriginalFilename());
                Files.write(caminho, bytes);
//                if (studentDTO != null) {
//                    studentDTO.setImageName(userDTO.getUsername() + file.getOriginalFilename());
//                    System.out.println("------> " + studentDTO.getImageName());
//                } else {
//                    // Lide com a situação em que studentDTO é nulo
//                    System.out.println("studentDTO é nulo!");
//                }
                student.setImageName(userDTO.getUsername() + file.getOriginalFilename());

            }
        } catch (IOException e) {
            // Handle IOException appropriately, e.g., log the error or throw a custom exception
            e.printStackTrace(); // This is for demonstration purposes; consider using a logging framework
        }
    }


    private void validEmail(UserDTO userDTO) {
        Optional<User> objEmail = userRepository.findByEmail(userDTO.getEmail());
        if (objEmail.isPresent() && objEmail.get().getId() != userDTO.getId()) {
            throw new DataIntegrityViolationException("Email já cadastrado no sistema!");
        }
        Optional<User> obj = userRepository.findByUsername(userDTO.getUsername());
        System.out.println(userDTO.getUsername());
        if (obj.isPresent() && obj.get().getId() != userDTO.getId()) {
            throw new DataIntegrityViolationException("Nome já cadastrado no sistema!!");
        }
    }


    @Transactional
    public void deleteEmployee(Long id) throws IOException {
        String imagePath = this.imagePath();

        // Check if the employee exists before deleting
        Student student = findById(id);

        if (student  != null) {
            // Delete the associated image, if it exists
            if (student .getImageName() != null) {
                Path caminho = Paths.get(imagePath + student .getImageName());
                Files.deleteIfExists(caminho);
            }

            // Delete the user and employee
            userRepository.deleteById(student.getUser().getId());

            studentRepository.deleteById(id);
        } else {
            if (student  == null) {
            } else {
                System.out.println("Administrator não pode ser deletado!.");
            }
        }
    }

    @Transactional
    public void softDeleteStudent(Long id) {
        Student student = findById(id);
        student.setDeleted(true);
        // Optionally, you can set other fields like the deletion date if needed.
        studentRepository.save(student);
    }

    public Student update(Long id, UserDTO userDTO, MultipartFile file) throws IOException {

        Optional<Student> studentId = studentRepository.findById(id);
        if (!studentId.isPresent()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
        }



        Optional<User> userId = userRepository.findById(studentId.get().getUser().getId());
        if (!userId.isPresent()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
        }
        String imagePath = this.imagePath();

        User user = userId.get();
        if (user.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }

        if (user.getRole() != null) {
            user.setRole(RoleEnum.ROLE_STUDENT);
        }

        if (user.getEmail() != null) {
            user.setEmail(userId.get().getEmail());
        }

        Student student = studentId.get();
//
        if (student.getName() != null) {
            student.setName(user.getUsername());
        }

//        if (teacher.getTelephone() != null) {
//            teacher.setTelephone(teacherDTO.getTelephone());
//        }

//        if (teacher.getDocumentId() != null) {
//            teacher.setDocumentId(teacherDTO.getDocumentId());
//        }

//
//        if (teacherDTO.getAddressDTO() != null) {
//            AddressDTO addressDTO = teacherDTO.getAddressDTO();
//            Address address = new Address();
//            address.setZipCode(addressDTO.getZipCode());
//            address.setStreet(addressDTO.getStreet());
//            address.setCity(addressDTO.getCity());
//            address.setState(addressDTO.getState());
//            address.setCountry(addressDTO.getCountry());
//            address.setDistrict(addressDTO.getDistrict());
//            address.setHouseNumber(addressDTO.getHouseNumber());
//            address.setComplement(addressDTO.getComplement());
//            teacher.setAddress(address);
//        }



        if (file != null && !file.isEmpty()) {
            Student studentImagem = findById(id);
            if (studentImagem  != null && studentImagem.getImageName() != null) {
                Path caminho = Paths.get(imagePath + studentImagem.getImageName());
                Files.deleteIfExists(caminho);
                this.createImage(userDTO,student, file);
                if (student.getImageName() != null) {
                    student.setImageName(student.getImageName());
                }
            }
        }
        return studentRepository.save(student);
    }

}

