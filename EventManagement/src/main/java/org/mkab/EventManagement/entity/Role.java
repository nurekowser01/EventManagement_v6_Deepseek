package org.mkab.EventManagement.entity;

import org.mkab.EventManagement.model.enums.RoleType;
import jakarta.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType type;

    private String details;
    
 // ✅ Add this constructor manually
    public Role(RoleType type, String details) {
        this.type = type;
        this.details = details;
    }
}
