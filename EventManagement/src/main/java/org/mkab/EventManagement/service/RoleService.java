package org.mkab.EventManagement.service;

import org.mkab.EventManagement.entity.Role;
import org.mkab.EventManagement.model.enums.RoleType;
import org.mkab.EventManagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByType(RoleType type) {
        return roleRepository.findByType(type);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(Long id, Role updatedRole) {
        return roleRepository.findById(id).map(role -> {
            role.setType(updatedRole.getType());
            role.setDetails(updatedRole.getDetails());
            return roleRepository.save(role);
        }).orElseThrow(() -> new RuntimeException("Role not found with id " + id));
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
