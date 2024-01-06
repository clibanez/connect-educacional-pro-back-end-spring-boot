package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.Token;
import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.AuthenticationDTO;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.model.enuns.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AutenticationService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;


    public Token register(UserDTO userDTO, String role) {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school  == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
//        userDTO.id(null);
        User user = userService.postUser(userDTO, role);
        user.setSchool(school);
        String jwt = jwtService.generateToken(user);
        return Token.builder().token(jwt).build();
    }


    public Token login(AuthenticationDTO authDTO) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password())
        );
        var user = this.userDetailsService.loadUserByUsername(authDTO.login());
        String jwtToken = jwtService.generateToken(user);
        return Token.builder().token(jwtToken).build();
    }


    //Login para Painel administrador
    public Token loginAdminPanel(AuthenticationDTO authDTO) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getLogin(), authDTO.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(RoleEnum.ROLE_SUPERADMIN.name())))
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authDTO.getLogin());

        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> RoleEnum.ROLE_SUPERADMIN.name().equals(authority.getAuthority()))) {
            String jwtToken = jwtService.generateToken(userDetails);
            return new Token(jwtToken);
        } else {
            // Usuário não tem a função ROLE_SUPERADMIN
            throw new RuntimeException("Usuário não autorizado");
        }
    }

}
