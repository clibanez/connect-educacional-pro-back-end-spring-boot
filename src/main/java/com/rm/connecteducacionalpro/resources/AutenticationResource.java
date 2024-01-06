package com.rm.connecteducacionalpro.resources;

import com.rm.connecteducacionalpro.config.UserAutenticantion;
import com.rm.connecteducacionalpro.model.Token;
import com.rm.connecteducacionalpro.model.dto.AuthenticationDTO;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.services.AutenticationService;
import com.rm.connecteducacionalpro.util.GeneralUtilies;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@Controller
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AutenticationResource extends Token {

    @Autowired
    private AutenticationService authService;

    @GetMapping("/role")
    public ResponseEntity<String> getUserRole() {
        School school = UserAutenticantion.authenticated().getSchool();
        if (school  == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Assumindo que o usuário tem apenas uma função (role)
        String role = authorities.iterator().next().getAuthority();

        return ResponseEntity.ok().body(role);
    }


    @GetMapping("/teacher/id")
    public ResponseEntity<Integer> getUserId() {
        Long teacherId = UserAutenticantion.authenticated().getTeacher().getId();
        if (teacherId == null) {
            throw new AuthorizationServiceException("Acesso negado!");
        }

        // Convertendo para int
        int teacherIdAsInt = teacherId.intValue();

        return ResponseEntity.ok().body(teacherIdAsInt);
    }



    //Metodo antigo
    @PostMapping("/register/{role}")
    public ResponseEntity<Token> register(@RequestBody UserDTO userDTO, @PathVariable String role) {
        URI uri = GeneralUtilies.toUri("/register/{role}");
        return ResponseEntity.created(uri).body(authService.register(userDTO, role));
    }


    //    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody AuthenticationDTO authDTO) {
        return ResponseEntity.ok().body(authService.login(authDTO));
    }

    //Login para Painel administrador
    @PostMapping("/login/admin-panel")
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<Token> loginAmin(@RequestBody AuthenticationDTO authDTO) {
        return ResponseEntity.ok().body(authService.loginAdminPanel(authDTO));
    }
}
