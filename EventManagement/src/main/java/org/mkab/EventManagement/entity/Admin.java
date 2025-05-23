package org.mkab.EventManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mkab.EventManagement.entity.Role;
import org.mkab.EventManagement.model.enums.RoleType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private String email;
    private String mobile;
    private LocalDate dateOfBirth;
    private String profileImage;

    
	private Boolean isActive = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private String lastLoginIp;

    @Column(columnDefinition = "TEXT")
    private String notes;
    
    // NEW FIELDS
    private String createdBy;
    private String updatedBy;

    private int loginAttempts = 0;

    private LocalDateTime lastPasswordChangeAt;

    // Relationships (e.g., Jamat, Roles)
    // Getters & Setters

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Schema(hidden = true)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "admin_roles",
        joinColumns = @JoinColumn(name = "admin_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private Set<Role> roles;
   

    
    public boolean isSuperAdmin() {
        if (this.roles == null || this.roles.isEmpty()) {
            return false;
        }
        return this.roles.stream()
                .anyMatch(role -> role.getType() == RoleType.SUPER_ADMIN);
    }


}
