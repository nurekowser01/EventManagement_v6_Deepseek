package org.mkab.EventManagement.repository;

import java.util.Optional;

import org.mkab.EventManagement.entity.Role;
import org.mkab.EventManagement.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByType(RoleType type);
}
