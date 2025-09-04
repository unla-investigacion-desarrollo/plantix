package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.repositories.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final IUserRepository userRepository;
    public UserDetailsServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
    este metodo es especifico de spring security. lo va a usar ese modulo del framework
    para traer usuarios de la bd y validarlos
    si por lo que sea necesitamos retornar usuarios pero con el fin de mostrarlos en un html
    o retornarlos en un json de la api, lo ideal seria crear otro metodo aparte para eso pese a
    que podamos castear el retorno de este metodo a un User (ya que User implementa UserDetails)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + username + " not found.")
        );
    }
}
