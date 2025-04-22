package org.mkab.EventManagement.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.management.relation.Role;

public class AdminResponseDTO {
    public Long id;
    public String username;
    public String name;
    public String email;
    public String phone;
    public LocalDate dateOfBirth;
    public String profileImage;
    public Set<String> jamatNames;
    public Set<Role> roles;
    public boolean isSuperAdmin;
}

