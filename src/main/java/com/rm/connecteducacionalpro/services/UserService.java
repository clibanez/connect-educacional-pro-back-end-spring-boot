package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.repositories.UserRepository;
import com.rm.connecteducacionalpro.services.exception.DataIntegrityViolationException;
import com.rm.connecteducacionalpro.util.GeneralUtilies;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserServiceImpl{

    private final UserRepository userRepo;
    private final EmailService emailServicee;


    public User findById(Long id){
        Optional<User> obj = userRepo.findById(id);
        return obj.orElseThrow();
    }

    public Page<User> getUser(int page, int size) {
        return userRepo.findAll(PageRequest.of(page, size));
    }

    //criar usuario
    public User create(UserDTO userDTO){
        School defesaCivil = UserAutenticantion.authenticated().getSchool();
        if (defesaCivil == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        this.validEmail(userDTO);

        User user = new User(userDTO);
//        User.setDate(LocalDateTime.now());
        user.setPassword(GeneralUtilies.encode(userDTO.getPassword()));
        user.setSchool(defesaCivil);
        return userRepo.save(user);
    }


    public User postUser(UserDTO userDTO, String role) {
//        School school = UserAutenticantion.authenticated().getSchool();
//        if (school == null) {
//            throw new AuthorizationServiceException("Acesso negado!");
//        }
//        this.validEmail(userDTO);
//        User user = (role.equals("ROLE_ADMIN")) ? userDTO.addAdmin(userDTO) : userDTO.addDutyOfficer(userDTO);
//        user.setSchool(school);
//        return userRepo.save(user);
        return null;
    }


    public User getUser(String username) {
        return userRepo.findByUsername(username).get();
    }

    @Override
    public User findById() {
        return null;
    }

    //Implementando service de recuperação de senha
    public String alterarSenha(UserDTO usuarioDTO) {
        User usuarioBranco = userRepo.findByEmailAndCodigoRecuperacaoSenha(usuarioDTO.getEmail(), usuarioDTO.getCodigoRecuperacaoSenha());

        if (usuarioBranco != null) {

            Date diferenca = new Date(new Date().getTime() - usuarioBranco.getDataEnvioCodigo().getTime());

            if (diferenca.getTime() / 100000 < 10000) {

                usuarioBranco.setPassword(GeneralUtilies.encode(usuarioDTO.getPassword()));

                usuarioBranco.setCodigoRecuperacaoSenha(null);
                userRepo.saveAndFlush(usuarioBranco);

                return "Senha alterada com sucesso!";

            } else {
                return "tempo expirado, solicite um novo código";
            }
        } else {
            return "email ou Código não encontrado!";
        }

    }


    //Implementando service envio de codigo para recuperar a senha
//    public String solicitarCodigo(String email){
//        Optional<User> usuario = userRepo.findByEmail(email);
//        usuario.get().setCodigoRecuperacaoSenha(getCodigoRecuperacaoSenha(Math.toIntExact(usuario.get().getId())));
//
//        System.out.println("---->" + getCodigoRecuperacaoSenha(Math.toIntExact(usuario.get().getId())));
//
//        usuario.get().setDataEnvioCodigo(new Date());
//        userRepo.saveAndFlush(usuario.get());
//        emailServicee.enviarEmailTexto(usuario.get().getEmail(),"Código de Recuperação de Senha","Olá, o seu código para recuperação de senha é o seguinte: " + usuario.get().getCodigoRecuperacaoSenha());
//        System.out.println(usuario.get().getEmail());
//        return "Código Enviado!";
//    }

    public String solicitarCodigo(String email){

        Optional<User> usuario = userRepo.findByEmail(email);

        int userId = Math.toIntExact(usuario.get().getId());
        String fullCodigo = getCodigoRecuperacaoSenha(userId);

        // Extract the last four digits from the full code
        String lastFourDigits = fullCodigo.substring(fullCodigo.length() - 4); //recupera os 4 ultimos digítos

        usuario.get().setCodigoRecuperacaoSenha(lastFourDigits);
        usuario.get().setDataEnvioCodigo(new Date());
        userRepo.saveAndFlush(usuario.get());

        emailServicee.enviarEmailTexto(usuario.get().getEmail(),"Código de Recuperação de Senha","Olá, o seu código para recuperação de senha é o seguinte: " + lastFourDigits);

        System.out.println(usuario.get().getEmail());
        return usuario.get().getEmail();
    }


    //Implementando service de recuperação de senha
    private String getCodigoRecuperacaoSenha(Integer id){
        DateFormat format = new SimpleDateFormat(("ddMMyyyyHHmmss"));
        return format.format(new Date())+id;
    }


    private void validEmail(UserDTO userDTO){
        Optional<User> obj = userRepo.findByEmail(userDTO.getEmail());
        if(obj.isPresent() && obj.get().getId() != userDTO.getId()){
            throw new DataIntegrityViolationException("Email já cadastrado no sistema!");
        }
    }
}

