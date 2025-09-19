package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.entities.User;
import com.laboratorio.iot.plantix.exceptions.user.*;
import com.laboratorio.iot.plantix.repositories.IUserRepository;
import com.laboratorio.iot.plantix.services.IUserService;
import com.laboratorio.iot.plantix.validator.UserValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, IUserService {
    private final IUserRepository userRepository;
    public UserDetailsServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
    este metodo es especifico de spring security. lo va a usar ese modulo del framework para manejar la autenticacion.
    si por lo que sea necesitas retornar usuarios pero del tipo User que declaramos para nuestra BD,
    usa el metodo que esta abajo de este uwu el que se llama findByEmail
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + username + " not found.")
        );
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findByDni(long dni) {
        return userRepository.findByDni(dni).orElse(null);
    }

    @Override
    public User save(User user) throws UserInvalidEmailException, UserInvalidDNIException, UserInvalidPasswordException {
        if(!UserValidator.thisEmailIsValid(user.getEmail())) throw new UserInvalidEmailException("Save operation failed. Invalid email.");
        if(!UserValidator.thisDNIIsValid(user.getDni())) throw new UserInvalidDNIException("Save operation failed. Invalid DNI.");
        if(!UserValidator.thisPasswordIsValid(user.getPassword())) throw new UserInvalidPasswordException("Save operation failed. Invalid password.");
        User userByEmail = findByEmail(user.getEmail());
        User userByDNI = findByDni(user.getDni());
        if(userByEmail != null) throw new UserInvalidEmailException("Save operation failed. Email already in use.");
        if(userByDNI != null) throw new UserInvalidEmailException("Save operation failed. DNI already in use.");

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
