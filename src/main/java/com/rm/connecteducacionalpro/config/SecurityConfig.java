package com.rm.connecteducacionalpro.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationFilter filter;
    @Autowired
    private AuthenticationProvider provider;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/authentication/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/solicitar-codigo").permitAll() //Liberar envio de código para o email
                .requestMatchers(HttpMethod.POST, "/users/alterar-senha").permitAll() //Liberar envio de alteração de senha
                .requestMatchers(HttpMethod.POST, "/authentication/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/school/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

//                .requestMatchers(HttpMethod.GET, "/active-sessions").permitAll() // Permitir conexões WebSocket
//                .requestMatchers(HttpMethod.DELETE, "/session/**").permitAll()

//                .requestMatchers(HttpMethod.GET, "/chat/websocket/**").permitAll() // Permitir conexões WebSocket
//                .requestMatchers(HttpMethod.GET, "/topic/**").permitAll() // Permitir tópicos WebSocket
//                .requestMatchers(HttpMethod.POST, "/chat/websocket/**").permitAll()
//                .requestMatchers("/chat/websocket/**", "/topic/**", "/app/**").permitAll()

                //WEBSOCKET

//                .requestMatchers(HttpMethod.GET, "/ws/**").permitAll()
//
//                .requestMatchers(HttpMethod.POST, "/topic/public/**").permitAll()
//                .requestMatchers(HttpMethod.DELETE, "/chat/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/defesa-civil/names/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/administradores").hasAuthority("ROLE_ADMINISTRADOR")
//                .requestMatchers(HttpMethod.GET, "/usuarios").hasAuthority("ROLE_USUARIO")

                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}