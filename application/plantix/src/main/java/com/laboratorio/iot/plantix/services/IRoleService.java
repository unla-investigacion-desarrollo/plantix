package com.laboratorio.iot.plantix.services;

import com.laboratorio.iot.plantix.entities.Role;

public interface IRoleService {
    Role findByRoleName(String roleName);
}
