package com.rm.connecteducacionalpro.model;

import com.rm.connecteducacionalpro.model.dto.UserDTO;
import com.rm.connecteducacionalpro.model.enuns.RoleEnum;
import com.rm.connecteducacionalpro.model.escola.School;
import com.rm.connecteducacionalpro.model.escola.cadastro.Employee;
import com.rm.connecteducacionalpro.model.escola.cadastro.Secretary;
import com.rm.connecteducacionalpro.model.escola.cadastro.Student;
import com.rm.connecteducacionalpro.model.escola.cadastro.Teacher;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

@Table(name = "users")
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    @Temporal(TemporalType.TIMESTAMP) //trabalhando com minutos.

//        private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private String codigoRecuperacaoSenha;

    @Temporal(TemporalType.TIMESTAMP) //trabalhando com minutos.
    private Date dataEnvioCodigo;


    @OneToOne(mappedBy = "user")
    private Employee employee;

    @OneToOne(mappedBy = "user")
    private Student student;

    @OneToOne(mappedBy = "user")
    private Teacher teacher;

    @OneToOne(mappedBy = "user")
    private Secretary secretary;

    public User(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.username = userDTO.getUsername();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.role = userDTO.getRole();
        this.school = userDTO.getSchool();
        this.codigoRecuperacaoSenha = userDTO.getCodigoRecuperacaoSenha();
        this.dataEnvioCodigo = userDTO.getDataEnvioCodigo();
    }

    public User(Long id, String username, String email, String password, RoleEnum role, School school,
                String codigoRecuperacaoSenha, Date dataEnvioCodigo, Student student) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.school = school;
        this.codigoRecuperacaoSenha = codigoRecuperacaoSenha;
        this.dataEnvioCodigo = dataEnvioCodigo;
        this.employee = employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
