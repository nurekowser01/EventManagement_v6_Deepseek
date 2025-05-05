package org.mkab.EventManagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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

    
}
