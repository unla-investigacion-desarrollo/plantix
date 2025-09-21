package com.laboratorio.iot.plantix.services.implementation;

import com.laboratorio.iot.plantix.entities.Role;
import com.laboratorio.iot.plantix.repositories.IRoleRepository;
import com.laboratorio.iot.plantix.services.IRoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;
    public RoleServiceImpl(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRole(roleName).orElse(null);
    }
}
