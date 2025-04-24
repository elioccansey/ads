package com.eli.ads.user.role;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
    RoleRepository roleRepository;

    @Override
    public Role findRoleByName(RoleEnum name) {
        return roleRepository.findByName(name)
                .orElseThrow(()-> new EntityNotFoundException("No role found with name " + name));
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }
}
