package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.entities.User;
import com.laboratorio.iot.plantix.exceptions.user.*;
import com.laboratorio.iot.plantix.repositories.IUserRepository;
import com.laboratorio.iot.plantix.services.IUserService;
import com.laboratorio.iot.plantix.validator.UserValidator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService, IUserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public User extractAuthenticatedUserFromSecurityContext() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User with email " + email + " not found.")
        );
    }

    @Override
    public User findByDni(long dni) throws UserNotFoundException {
        return userRepository.findByDni(dni).orElseThrow(() ->
                new UserNotFoundException("User with DNI " + dni + " not found.")
        );
    }

    @Override
    public User save(User user) throws UserInvalidEmailException, UserInvalidDNIException, UserInvalidPasswordException {
        if(!UserValidator.thisEmailIsValid(user.getEmail()))
            throw new UserInvalidEmailException("Save operation failed. Invalid email.");
        if(!UserValidator.thisDNIIsValid(user.getDni()))
            throw new UserInvalidDNIException("Save operation failed. Invalid DNI.");
        if(!UserValidator.thisPasswordIsValid(user.getPassword()))
            throw new UserInvalidPasswordException("Save operation failed. Invalid password.");
        if(existsByEmail(user.getEmail()))
            throw new UserInvalidEmailException("Save operation failed. Email already in use.");
        if(existsByDni(user.getDni()))
            throw new UserInvalidDNIException("Save operation failed. DNI already in use.");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getId() == null)
            user.setRegistrationDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(long dni) {
        return userRepository.existsByDni(dni);
    }
}
