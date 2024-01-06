package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.TeacherDTO;
import com.rm.connecteducacionalpro.model.enuns.RoleEnum;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import com.rm.connecteducacionalpro.repositories.TeacherRepository;
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

@Service
public class TeacherService {
    @Autowired
    public TeacherRepository teacherRepository;
    @Autowired
    public UserRepository userRepository;

//    @Autowired
//    public TurmaRepository turmaRepository;



    public byte[] imageTeacher(String image) throws IOException {
        String imagePath = this.imagePath();
        File imagemArquivo = new File(imagePath + image);
        if (image != null || image.trim().length() > 0) {
            return Files.readAllBytes(imagemArquivo.toPath());
        }
        return null;
    }


    public Teacher findById(Long id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        return teacher.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Teacher> findAll() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        List<Teacher> teacher = teacherRepository.findByUserSchool(school);
        return teacher;
    }


    public Teacher create(UserDTO userDTO, TeacherDTO teacherDTO, MultipartFile file) {
        this.validEmail(userDTO);
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        User user = new User(userDTO);
        user.setId(null);
        user.setSchool(school);
        user.setPassword(GeneralUtilies.encode("123456"));
        user.setRole(RoleEnum.ROLE_TEACHER);
        User userId = userRepository.save(user);

        Teacher teacher = new Teacher();
        this.createImage(userDTO, teacher, file);
        teacher.setId(null);
        teacher.setName(userId.getUsername());
        teacher.setUser(userId);
        // Save employee
        return teacherRepository.save(teacher);
    }


//    @Transactional
//    public void deleteteacher(Long id) throws IOException {
//        String imagePath = this.imagePath();
//
//        Teacher teacher = findById(id);
//
//        if (teacher != null) {
//            if (teacher.getImageName() != null) {
//                Path caminho = Paths.get(imagePath + teacher.getImageName());
//                Files.deleteIfExists(caminho);
//            }
//
//
//            userRepository.deleteById(teacher.getUser().getId());
//
//            teacherRepository.deleteById(id);
//        } else {
//            if (teacher == null) {
//            } else {
//                System.out.println("Administrator não pode ser deletado!.");
//            }
//        }
//    }

    @Transactional
    public void deleteteacher(Long id) throws IOException {
        String imagePath = this.imagePath();

        Teacher teacher = findById(id);

        if (teacher != null) {
            // Remove matriculas associadas à turma
//            turmaRepository.deleteByIdAndMatriculasIsEmpty(id);

            // Delete associated user
            userRepository.deleteById(teacher.getUser().getId());

            // Delete teacher
            teacherRepository.deleteById(id);

            // Delete teacher's image file
            if (teacher.getImageName() != null) {
                Path caminho = Paths.get(imagePath + teacher.getImageName());
                Files.deleteIfExists(caminho);
            }
        } else {
            // Handle the case where the teacher was not found
        }
    }




    public String imagePath() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        String imagePath = "./connect educacional pro/" + school.getName() + "/Foto Professores/";
        File directory = new File(imagePath);
        if (!directory.exists()) {
            // Se o diretório não existir, cria-o
            if (directory.mkdirs()) {
            }
        }
        return imagePath;
    }

    public void createImage(UserDTO userDTO, Teacher teacher, MultipartFile file) {
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
                teacher.setImageName(userDTO.getUsername() + file.getOriginalFilename());

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

    public Teacher update(Long id, UserDTO userDTO, MultipartFile file) throws IOException {

        Optional<Teacher> teacherId = teacherRepository.findById(id);
        if (!teacherId.isPresent()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
        }

        Optional<User> userId = userRepository.findById(teacherId.get().getUser().getId());
        if (!userId.isPresent()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
        }
        String imagePath = this.imagePath();

        User user = userId.get();
        if (user.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }

        if (user.getRole() != null) {
            user.setRole(RoleEnum.ROLE_TEACHER);
        }

        if (user.getEmail() != null) {
            user.setEmail(userId.get().getEmail());
        }

        Teacher teacher = teacherId.get();
//
        if (teacher.getName() != null) {
            teacher.setName(user.getUsername());
        }

        if (teacher.getUser() != null) {
            teacher.setUser(user);
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
            Teacher teacherImagem = findById(id);
            if (teacherImagem  != null && teacherImagem.getImageName() != null) {
                Path caminho = Paths.get(imagePath + teacherImagem.getImageName());
                Files.deleteIfExists(caminho);
                this.createImage(userDTO,teacher, file);
                if (teacher.getImageName() != null) {
                    teacher.setImageName(teacher.getImageName());
                }
            }
        }
        return teacherRepository.save(teacher);
    }

    //esse esta completo
//    public Teacher update(Long id, UserDTO userDTO, TeacherDTO teacherDTO, MultipartFile file) throws IOException {
//
//        Optional<Teacher> teacherId = teacherRepository.findById(id);
//        if (!teacherId.isPresent()) {
//            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
//        }
//
//        Optional<User> userId = userRepository.findById(id);
//        if (!userId.isPresent()) {
//            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
//        }
//        String imagePath = this.imagePath();
//
//        User user = userId.get();
//        if (user.getUsername() != null) {
//            user.setUsername(userDTO.getUsername());
//        }
//
//        if (user.getRole() != null) {
//            user.setRole(userDTO.getRole());
//        }
//
//        if (user.getEmail() != null) {
//            user.setEmail(userId.get().getEmail());
//        }
//
//        Teacher teacher = teacherId.get();
////
//        if (teacher.getName() != null) {
//            teacher.setName(user.getUsername());
//        }
//
////        if (teacher.getTelephone() != null) {
////            teacher.setTelephone(teacherDTO.getTelephone());
////        }
//
////        if (teacher.getDocumentId() != null) {
////            teacher.setDocumentId(teacherDTO.getDocumentId());
////        }
//
////
////        if (teacherDTO.getAddressDTO() != null) {
////            AddressDTO addressDTO = teacherDTO.getAddressDTO();
////            Address address = new Address();
////            address.setZipCode(addressDTO.getZipCode());
////            address.setStreet(addressDTO.getStreet());
////            address.setCity(addressDTO.getCity());
////            address.setState(addressDTO.getState());
////            address.setCountry(addressDTO.getCountry());
////            address.setDistrict(addressDTO.getDistrict());
////            address.setHouseNumber(addressDTO.getHouseNumber());
////            address.setComplement(addressDTO.getComplement());
////            teacher.setAddress(address);
////        }
//
//
//
//        if (file != null && !file.isEmpty()) {
//            Teacher teacherImagem = findById(id);
//            if (teacherImagem  != null && teacherImagem.getImageName() != null) {
//                Path caminho = Paths.get(imagePath + teacherImagem.getImageName());
//                Files.deleteIfExists(caminho);
//                this.createImage(userDTO,teacher, file);
//                if (teacher.getImageName() != null) {
//                    teacher.setImageName(teacherDTO.getImageName());
//                }
//            }
//        }
//        return teacherRepository.save(teacher);
//    }
}
