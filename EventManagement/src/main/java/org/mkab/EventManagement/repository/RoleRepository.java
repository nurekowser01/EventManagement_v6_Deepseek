package org.mkab.EventManagement.repository;

import java.util.Optional;

import org.mkab.EventManagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);  // Find role by its name
}
