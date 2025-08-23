package com.laboratorio.iot.plantix.configuration.security;

import com.laboratorio.iot.plantix.services.implementation.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
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
                            } else { // manejamos acceso no autenticado a traves de un navegador web O_______o
                                // redirigir a la view correspondiente del login:
                                // response.sendRedirect("/login"); , por ej.
                            }
                        })
                        // a partir de aca el usuario ya está autenticado c:
                        // si se dispara algun error, es por falta de autorizacion (permisos)
                        // redirigir a una pagina que explique que el usuario no tiene permisos (error 403) para acceder a ese recurso:
                        // .accessDeniedPage(ViewHelper.ACCESS_DENIED); , por ej.
                )
                .csrf(csrf -> csrf.disable()) // es discutible a futuro :c , pero como ahora manejamos sesiones stateless con http basic, no nos tenemos que preocupar por este tipo de ataque
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(httpRequest -> {
                    /*
                    cuando tengamos los endpoints en los controllers, aca vamos a definir
                    que roles y permisos se requieren para pegarle a ciertas rutas o___o
                    por ej, si queremos que solo usuarios autenticados pueden pegarle a los endpoints que arranquen con /field/:
                    httpRequest.requestMatchers("/field/**").authenticated();
                     */
                })
                .formLogin(login -> {
                    /*
                    aca simplemente hay que indicarle a spring security las rutas que vamos a usar
                    para manejar el login de los usuarios
                    dejo comentado codigo de ejemplo de Ticketo, un sistema hecho por Emi
                    cuando tengamos las rutas del login por parte de los controllers, podemos
                    descomentar este codigo y reemplazar las rutas por las de plantix
                     */
                    //login.loginPage("/auth/login");
                    //login.loginProcessingUrl("/auth/loginProcess");//POST
                    //login.usernameParameter("username");
                    //login.passwordParameter("password");
                    //login.defaultSuccessUrl("/auth/loginSuccess", true);
                    //login.permitAll();
                })
                .logout(logout -> {
                    /*
                    misma idea que con el metodo anterior a este, pero para procesar el logout
                     */
                    //logout.logoutUrl("/auth/logout");//POST
                    //logout.logoutSuccessUrl("/auth/login?logout=true");
                    //logout.permitAll();
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
