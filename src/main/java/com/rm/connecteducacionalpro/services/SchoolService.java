package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.EmployeeDTO;
import com.rm.connecteducacionalpro.model.dto.escola.SchoolDTO;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.model.enuns.RoleEnum;
import com.rm.connecteducacionalpro.repositories.EmployeeRepository;
import com.rm.connecteducacionalpro.repositories.SchoolRepository;
import com.rm.connecteducacionalpro.repositories.UserRepository;
import com.rm.connecteducacionalpro.services.exception.DataIntegrityViolationException;
import com.rm.connecteducacionalpro.services.exception.ObjectNotFoundException;
import com.rm.connecteducacionalpro.util.GeneralUtilies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
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
public class SchoolService {
    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserService userService;

    public School getSchoolById(Long id) {
        Optional<School> school = schoolRepository.findById(id);
        return school.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + school.get().getId()));
    }

    public School create(SchoolDTO schoolDTO,
                         UserDTO userDTO,
                         EmployeeDTO employeeDTO,
                         MultipartFile fileSchool,
                         MultipartFile fileEmployee
                        ) {

        String imagePath = "./connect educacional pro/" + schoolDTO.getName() + "/Foto funcionarios/";
        File directory = new File(imagePath);
        if (!directory.exists()) {
            // Se o diretório não existir, cria-o
            if (directory.mkdirs()) {
            }
        }


        try {
            if (!fileSchool.isEmpty()) {
                byte[] bytes = fileSchool.getBytes();
                Path caminho = Paths.get(imagePath + schoolDTO.getName() + fileSchool.getOriginalFilename());
                Files.write(caminho, bytes);
                schoolDTO.setImageName(schoolDTO.getName() + fileSchool.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        School school = new School(schoolDTO);

        this.validEmail(userDTO);
        // Salvar DefesaCivil
        School schoolId = schoolRepository.save(school);
        if (schoolId == null) {

            return null;
        }

        User user = new User(userDTO);
        user.setId(null);
        user.setSchool(schoolId);
        user.setRole(RoleEnum.ROLE_DIRECTOR);
        user.setPassword(GeneralUtilies.encode("123456"));

        // Salvar User
        User userId = userRepository.save(user);


        if (user == null) {
            // Manipular erro ao salvar User
            // Você pode lançar uma exceção, fazer log, etc.
            // Rollback: Excluir DefesaCivil salvo anteriormente
            schoolRepository.delete(schoolId);
            return null;
        }

        //Cria a pasta que vai ficar todas as imagens
//        String imagePath = "./connect educacional pro/" + schoolId.getName() + "/Foto usuarios/";
//        File directory = new File(imagePath);

        // Verificar se o diretório existe ou foi criado com sucesso
        if (!directory.exists() && !directory.mkdirs()) {
            // Manipular erro ao criar o diretório
            // Você pode lançar uma exceção, fazer log, etc.
            // Rollback: Excluir DefesaCivil e User salvos anteriormente
            schoolRepository.delete(schoolId);
            userRepository.delete(userId);
            return null;
        }

        try {
            if (!fileEmployee.isEmpty()) {
                byte[] bytes = fileEmployee.getBytes();
                Path caminho = Paths.get(imagePath + employeeDTO.getName() + fileEmployee.getOriginalFilename());
                Files.write(caminho, bytes);
                employeeDTO.setImageName(employeeDTO.getName() + fileEmployee.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Manipular erro ao escrever o arquivo
            // Você pode lançar uma exceção, fazer log, etc.
            // Rollback: Excluir DefesaCivil, User e diretório criado anteriormente
            schoolRepository.delete(schoolId);
            userRepository.delete(userId);
            deleteDirectory(directory);
            return null;
        }

        // Salvar Employee
        Employee employee = new Employee(employeeDTO);
        employee.setId(null);
        employee.setName(userDTO.getUsername());
        employee.setUser(userId);
        employee.getAddress().setCountry(schoolId.getAddress().getCountry()); // Mocado

        // Salvar Employee apenas se a etapa anterior for bem-sucedida
        Employee savedEmployee = employeeRepository.save(employee);
        if (savedEmployee == null) {
            // Manipular erro ao salvar Employee
            // Você pode lançar uma exceção, fazer log, etc.
            // Rollback: Excluir DefesaCivil, User, diretório e arquivo criados anteriormente
            schoolRepository.delete(schoolId);
            userRepository.delete(userId);
            deleteDirectory(directory);
            deleteFile(imagePath + employeeDTO.getImageName());
            return null;
        }

        return schoolRepository.save(school);
    }

    private void deleteDirectory(File directory) {
        if (directory != null && directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            directory.delete();
        }
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }


    public School findAllAutentication() {
        School c = UserAutenticantion.authenticated().getSchool();
        if (c == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        Optional<School> schools = schoolRepository.findById(c.getId());
        return schools.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + schools.get().getId()));
    }

    public List<School> findAll() {
        School defesaCivilId = UserAutenticantion.authenticated().getSchool();
        if (defesaCivilId == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        List<School> defesaCivil = schoolRepository.findAll();

        return defesaCivil;
    }

    public List<String> getAllDefesaCivilNames() {
        List<School> defesaCivilList = schoolRepository.findAll();
        return defesaCivilList.stream()
                .map(SchoolDTO::new)
                .map(SchoolDTO::getName)
                .collect(Collectors.toList());
    }



    public String imagePath() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        String imagePath = "./connect educacional pro/" + school.getName() + "/Foto funcionarios/";
        File directory = new File(imagePath);
        if (!directory.exists()) {
            // Se o diretório não existir, cria-o
            if (directory.mkdirs()) {
            }
        }
        return imagePath;
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


    public byte[] getEmployeeImage(String imageName) throws IOException {
        String imagePath = this.imagePath();
        File imageFile = new File(imagePath + imageName);
        if (imageName != null && imageName.trim().length() > 0) {
            return Files.readAllBytes(imageFile.toPath());
        }

        return null;
    }



}



