package org.mkab.EventManagement.dto;

import java.time.LocalDate;
import java.util.Set;

public class AdminResponseDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String mobile;
    private LocalDate dateOfBirth;
    private String profileImage;
    private Set<String> jamatNames;
    // private Set<RoleDTO> roles;  // Changed from Role to RoleDTO for correct serialization
    private boolean isSuperAdmin;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Set<String> getJamatNames() {
        return jamatNames;
    }

    public void setJamatNames(Set<String> jamatNames) {
        this.jamatNames = jamatNames;
    }

//    public Set<RoleDTO> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<RoleDTO> roles) {
//        this.roles = roles;
//    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public AdminResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminResponseDTO(Long id, String username, String name, String email, String mobile, LocalDate dateOfBirth,
		String profileImage, Set<String> jamatNames, boolean isSuperAdmin) {
	super();
	this.id = id;
	this.username = username;
	this.name = name;
	this.email = email;
	this.mobile = mobile;
	this.dateOfBirth = dateOfBirth;
	this.profileImage = profileImage;
	this.jamatNames = jamatNames;
	this.isSuperAdmin = isSuperAdmin;
}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }
}
