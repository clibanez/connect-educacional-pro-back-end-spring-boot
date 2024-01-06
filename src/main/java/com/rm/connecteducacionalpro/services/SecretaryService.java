package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.model.dto.escola.cadastro.SecretaryDTO;
import com.rm.connecteducacionalpro.model.enuns.RoleEnum;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Secretary;
import com.rm.connecteducacionalpro.repositories.SecretaryRepository;

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
public class SecretaryService {
    @Autowired
    public SecretaryRepository secretaryRepository;
    @Autowired
    public UserRepository userRepository;

//    @Autowired
//    public TurmaRepository turmaRepository;



    public byte[] imageSecretary(String image) throws IOException {
        String imagePath = this.imagePath();
        File imagemArquivo = new File(imagePath + image);
        if (image != null || image.trim().length() > 0) {
            return Files.readAllBytes(imagemArquivo.toPath());
        }
        return null;
    }


    public Secretary findById(Long id) {
        Optional<Secretary> secretary = secretaryRepository.findById(id);
        return secretary.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Secretary> findAll() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        List<Secretary> secretary = secretaryRepository.findByUserSchool(school);
        return secretary;
    }


    public Secretary create(UserDTO userDTO, SecretaryDTO SecretaryDTO, MultipartFile file) {
        this.validEmail(userDTO);
        School school = UserAutenticantion.authenticated().getSchool();
        if (school == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        User user = new User(userDTO);
        user.setId(null);
        user.setSchool(school);
        user.setPassword(GeneralUtilies.encode("123456"));
        user.setRole(RoleEnum.ROLE_SECRETARY);
        User userId = userRepository.save(user);

        Secretary secretary = new Secretary();
        this.createImage(userDTO, secretary, file);
        secretary.setId(null);
        secretary.setName(userId.getUsername());
        secretary.setUser(userId);
        // Save employee
        return secretaryRepository.save(secretary);
    }

//    @Transactional
//    public void deleteSecretary(Long id) throws IOException {
//        String imagePath = this.imagePath();
//
//        Secretary Secretary = findById(id);
//
//        if (Secretary != null) {
//            if (Secretary.getImageName() != null) {
//                Path caminho = Paths.get(imagePath + Secretary.getImageName());
//                Files.deleteIfExists(caminho);
//            }
//
//
//            userRepository.deleteById(Secretary.getUser().getId());
//
//            SecretaryRepository.deleteById(id);
//        } else {
//            if (Secretary == null) {
//            } else {
//                System.out.println("Administrator não pode ser deletado!.");
//            }
//        }
//    }

    @Transactional
    public void deleteSecretary(Long id) throws IOException {
        String imagePath = this.imagePath();

        Secretary Secretary = findById(id);

        if (Secretary != null) {
            // Remove matriculas associadas à turma
//            turmaRepository.deleteByIdAndMatriculasIsEmpty(id);

            // Delete associated user
            userRepository.deleteById(Secretary.getUser().getId());

            // Delete Secretary
            secretaryRepository.deleteById(id);

            // Delete Secretary's image file
            if (Secretary.getImageName() != null) {
                Path caminho = Paths.get(imagePath + Secretary.getImageName());
                Files.deleteIfExists(caminho);
            }
        } else {
            // Handle the case where the Secretary was not found
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

    public void createImage(UserDTO userDTO, Secretary secretary, MultipartFile file) {
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
                secretary.setImageName(userDTO.getUsername() + file.getOriginalFilename());

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

    public Secretary update(Long id, UserDTO userDTO, MultipartFile file) throws IOException {

        Optional<Secretary> secretaryId = secretaryRepository.findById(id);
        if (!secretaryId.isPresent()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
        }

        Optional<User> userId = userRepository.findById(secretaryId.get().getUser().getId());
        if (!userId.isPresent()) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id);
        }
        String imagePath = this.imagePath();

        User user = userId.get();
        if (user.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }

        if (user.getRole() != null) {
            user.setRole(RoleEnum.ROLE_SECRETARY);
        }

        if (user.getEmail() != null) {
            user.setEmail(userId.get().getEmail());
        }

        Secretary secretary = secretaryId.get();
//
        if (secretary.getName() != null) {
            secretary.setName(user.getUsername());
        }

        if (secretary.getUser() != null) {
            secretary.setUser(user);
        }

//        if (Secretary.getTelephone() != null) {
//            Secretary.setTelephone(SecretaryDTO.getTelephone());
//        }

//        if (Secretary.getDocumentId() != null) {
//            Secretary.setDocumentId(SecretaryDTO.getDocumentId());
//        }

//
//        if (SecretaryDTO.getAddressDTO() != null) {
//            AddressDTO addressDTO = SecretaryDTO.getAddressDTO();
//            Address address = new Address();
//            address.setZipCode(addressDTO.getZipCode());
//            address.setStreet(addressDTO.getStreet());
//            address.setCity(addressDTO.getCity());
//            address.setState(addressDTO.getState());
//            address.setCountry(addressDTO.getCountry());
//            address.setDistrict(addressDTO.getDistrict());
//            address.setHouseNumber(addressDTO.getHouseNumber());
//            address.setComplement(addressDTO.getComplement());
//            Secretary.setAddress(address);
//        }



        if (file != null && !file.isEmpty()) {
            Secretary SecretaryImagem = findById(id);
            if (SecretaryImagem  != null && SecretaryImagem.getImageName() != null) {
                Path caminho = Paths.get(imagePath + SecretaryImagem.getImageName());
                Files.deleteIfExists(caminho);
                this.createImage(userDTO,secretary, file);
                if (secretary.getImageName() != null) {
                    secretary.setImageName(secretary.getImageName());
                }
            }
        }
        return secretaryRepository.save(secretary);
    }

    //esse esta completo
//    public Secretary update(Long id, UserDTO userDTO, SecretaryDTO SecretaryDTO, MultipartFile file) throws IOException {
//
//        Optional<Secretary> SecretaryId = SecretaryRepository.findById(id);
//        if (!SecretaryId.isPresent()) {
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
//        Secretary Secretary = SecretaryId.get();
////
//        if (Secretary.getName() != null) {
//            Secretary.setName(user.getUsername());
//        }
//
////        if (Secretary.getTelephone() != null) {
////            Secretary.setTelephone(SecretaryDTO.getTelephone());
////        }
//
////        if (Secretary.getDocumentId() != null) {
////            Secretary.setDocumentId(SecretaryDTO.getDocumentId());
////        }
//
////
////        if (SecretaryDTO.getAddressDTO() != null) {
////            AddressDTO addressDTO = SecretaryDTO.getAddressDTO();
////            Address address = new Address();
////            address.setZipCode(addressDTO.getZipCode());
////            address.setStreet(addressDTO.getStreet());
////            address.setCity(addressDTO.getCity());
////            address.setState(addressDTO.getState());
////            address.setCountry(addressDTO.getCountry());
////            address.setDistrict(addressDTO.getDistrict());
////            address.setHouseNumber(addressDTO.getHouseNumber());
////            address.setComplement(addressDTO.getComplement());
////            Secretary.setAddress(address);
////        }
//
//
//
//        if (file != null && !file.isEmpty()) {
//            Secretary SecretaryImagem = findById(id);
//            if (SecretaryImagem  != null && SecretaryImagem.getImageName() != null) {
//                Path caminho = Paths.get(imagePath + SecretaryImagem.getImageName());
//                Files.deleteIfExists(caminho);
//                this.createImage(userDTO,Secretary, file);
//                if (Secretary.getImageName() != null) {
//                    Secretary.setImageName(SecretaryDTO.getImageName());
//                }
//            }
//        }
//        return SecretaryRepository.save(Secretary);
//    }
}
