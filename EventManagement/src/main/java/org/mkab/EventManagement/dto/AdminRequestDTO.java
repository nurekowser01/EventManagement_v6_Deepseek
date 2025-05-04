package org.mkab.EventManagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Data;
@Data
public class AdminRequestDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private String mobile;
    private LocalDate dateOfBirth;
    private String profileImage;
    private Set<Long> jamatIds;
//    private Set<RoleDTO> roles;  // Using RoleDTO instead of javax.management.relation.Role
    private boolean isSuperAdmin;
    
 

	// ✅ NEW FIELDS
    private Boolean isActive;
    private String notes;
    private LocalDateTime lastLoginAt;
    private String lastLoginIp;
    
   
}
