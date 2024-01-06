package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.Address;
import com.rm.connecteducacionalpro.model.enuns.RoleEnum;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.AddressDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.EmployeeDTO;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.repositories.EmployeeRepository;
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
public class EmployeeService {

    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    public UserRepository userRepository;

    public byte[] getEmployeeImage(String imageName) throws IOException {
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


    public Employee findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Employee> findAll() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        List<Employee> employee = employeeRepository.findByUserSchool(school);

        return employee;
    }


    //O.B.S junta o username com employeename lembrado que tem que mudar o nome da imagem salva
    public Employee create(UserDTO userDTO, EmployeeDTO employeeDTO, MultipartFile file) {

        this.validEmail(userDTO);
        this.createImage(userDTO, employeeDTO, file);
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        User user = new User(userDTO);
        user.setId(null);
        user.setSchool(school);
        user.setPassword(GeneralUtilies.encode("123456"));
        if(user.getRole() == null){
            user.setRole(RoleEnum.ROLE_DIRECTOR);
        }


        // Save user
        User userId = userRepository.save(user);
        Employee employee = new Employee(employeeDTO);
        employee.setId(null);
        employee.setName(userId.getUsername());
        employee.setUser(userId);

        // Save employee
        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) throws IOException {
        String imagePath = this.imagePath();

        // Check if the employee exists before deleting
        Employee employee = findById(id);

        if (employee != null) {
            // Delete the associated image, if it exists
            if (employee.getImageName() != null) {
                Path caminho = Paths.get(imagePath + employee.getImageName());
                Files.deleteIfExists(caminho);
            }

            // Delete the user and employee
            userRepository.deleteById(employee.getUser().getId());
            employeeRepository.deleteById(id);
        } else {
            // The employee was not found or has ROLE_ADMIN
            // Handle this case as needed, e.g., throw an exception or log a message
            if (employee == null) {
                // Handle the case where the employee was not found
                // You can throw an exception, log a message, or perform other actions
            } else {
                // Handle the case where the employee has ROLE_ADMIN
                // You can send a message, throw an exception, log a message, or perform other actions
                System.out.println("Administrator não pode ser deletado!.");
            }
        }
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

    public void createImage(UserDTO userDTO, EmployeeDTO employeeDTO, MultipartFile file) {
        try {
            String imagePath = this.imagePath();
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path caminho = Paths.get(imagePath + userDTO.getUsername() + file.getOriginalFilename());
                Files.write(caminho, bytes);
                employeeDTO.setImageName(userDTO.getUsername() + file.getOriginalFilename());
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

    public Employee update(Long id, UserDTO userDTO, EmployeeDTO employeeDTO, MultipartFile file) throws IOException {

        Optional<Employee> employeeId = employeeRepository.findById(id);
        if (!employeeId.isPresent()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
        }

        Optional<User> userId = userRepository.findById(employeeId.get().getUser().getId());
        if (!userId.isPresent()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
        }
        String imagePath = this.imagePath();

        User user = userId.get();
        if (user.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }

        if (user.getRole() != null) {
            user.setRole(userDTO.getRole());
        }

        System.out.println("-------> " + userId.get().getEmail());

        if (user.getEmail() != null) {
            user.setEmail(userId.get().getEmail());
        }

        Employee employee = employeeId.get();
//
        if (employee.getName() != null) {
            employee.setName(user.getUsername());
        }

        if (employee.getOffice() != null) {
            employee.setOffice(employeeDTO.getOffice());
        }

        if (employee.getEmployeeFunction() != null) {
            employee.setEmployeeFunction(employeeDTO.getEmployeeFunction());
        }

        if (employee.getTelephone() != null) {
            employee.setTelephone(employeeDTO.getTelephone());
        }

        if (employee.getDocumentId() != null) {
            employee.setDocumentId(employeeDTO.getDocumentId());
        }


        if (employeeDTO.getAddressDTO() != null) {
            AddressDTO addressDTO = employeeDTO.getAddressDTO();
            Address address = new Address();
            address.setZipCode(addressDTO.getZipCode());
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setCountry(addressDTO.getCountry());
            address.setDistrict(addressDTO.getDistrict());
            address.setHouseNumber(addressDTO.getHouseNumber());
            address.setComplement(addressDTO.getComplement());
            employee.setAddress(address);
        }



        if (file != null && !file.isEmpty()) {
            Employee employeeImagem = findById(id);
            if (employeeImagem  != null && employeeImagem.getImageName() != null) {
                Path caminho = Paths.get(imagePath + employeeImagem.getImageName());
                Files.deleteIfExists(caminho);
                this.createImage(userDTO,employeeDTO, file);
                if (employee.getImageName() != null) {
                    employee.setImageName(employeeDTO.getImageName());
                }
            }
        }
        return employeeRepository.save(employee);
    }
}


