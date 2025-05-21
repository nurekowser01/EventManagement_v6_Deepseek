package org.mkab.EventManagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.mkab.EventManagement.model.enums.RoleType;

import lombok.Data;

@Data
public class AdminResponseDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String mobile;
    private LocalDate dateOfBirth;
    private String profileImage;
    private Set<String> jamatNames;
    private boolean isSuperAdmin;
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    // ✅ NEW FIELDS
    private Boolean isActive;
    private String notes;
    private LocalDateTime lastLoginAt;
    private String lastLoginIp;
    
 // NEW FIELDS
    private String createdBy;
    private String updatedBy;

    private int loginAttempts = 0;

    private LocalDateTime lastPasswordChangeAt;
    private Set<RoleType> roles;  // or List<RoleType>
}
