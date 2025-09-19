package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.entities.User;
import com.laboratorio.iot.plantix.exceptions.user.*;

import java.util.List;

public interface IUserService {
    User findByEmail(String email);
    User findByDni(long dni);
    User save(User user) throws UserInvalidEmailException, UserInvalidDNIException, UserInvalidPasswordException;
    List<User> findAll();
}
