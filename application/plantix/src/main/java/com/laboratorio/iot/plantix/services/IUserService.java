package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.entities.User;
import com.laboratorio.iot.plantix.exceptions.user.*;

import java.util.List;

public interface IUserService {
    User extractAuthenticatedUserFromSecurityContext();
    User findByEmail(String email) throws UserNotFoundException;
    User findByDni(long dni) throws UserNotFoundException;
    User save(User user) throws UserInvalidEmailException, UserInvalidDNIException, UserInvalidPasswordException;
    List<User> findAll();
    boolean existsByEmail(String email);
    boolean existsByDni(long dni);
}
