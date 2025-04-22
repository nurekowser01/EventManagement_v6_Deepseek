package org.mkab.EventManagement.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.management.relation.Role;

public class AdminRequestDTO {
    public String username;
    public String password;
    public String name;
    public String email;
    public String phone;
    public LocalDate dateOfBirth;
    public String profileImage;
    public Set<Long> jamatIds;
    public Set<Role> roles;
    public boolean isSuperAdmin;
}
