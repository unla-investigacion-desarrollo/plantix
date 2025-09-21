package com.laboratorio.iot.plantix.configuration.security;

import com.laboratorio.iot.plantix.services.implementation.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            if(request.getRequestURI().startsWith("/api/")) { // manejamos acceso no autenticado a la api (algun llamado desde postman, por ejemplo)
                                // como es el llamado a la api, no tiene sentido involucrar html. solo enviamos una respuesta en formato json
                                response.setContentType("application/json");
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("{\"error\": \"Unauthorized\"}");
                            } else {
                                // manejamos acceso no autenticado a traves de un navegador web O_______o
                                response.sendRedirect("/auth/login");
                            }
                        })
                )
                .csrf(csrf -> csrf.disable()) // es discutible a futuro :c , pero como ahora manejamos sesiones stateless con http basic, no nos tenemos que preocupar por este tipo de ataque
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(httpRequest -> {
                    httpRequest.requestMatchers("/css/**", "/img/**", "/js/**", "/libs/**").permitAll();
                    httpRequest.requestMatchers("/templates/fragments/**").permitAll();
                    httpRequest.requestMatchers("/auth/**").permitAll();
                    httpRequest.requestMatchers("/").permitAll();
                    httpRequest.requestMatchers("/fields/list").permitAll();
                    httpRequest.requestMatchers("/fields/*/detail").permitAll();
                    httpRequest.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.loginPage("/auth/login");
                    login.loginProcessingUrl("/auth/login-process");//POST
                    login.usernameParameter("username");
                    login.passwordParameter("password");
                    //ya no es necesario:3 si lo comentamos spring security se encarga de la redireccion
                    //login.defaultSuccessUrl("/auth/login-success", true);
                    login.permitAll();
                })
                .logout(logout -> {
                    logout.logoutUrl("/auth/logout");
                    logout.logoutSuccessUrl("/auth/login");
                    logout.permitAll();
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserServiceImpl userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
