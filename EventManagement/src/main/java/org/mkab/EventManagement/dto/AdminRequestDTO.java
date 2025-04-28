package org.mkab.EventManagement.dto;

import java.time.LocalDate;
import java.util.Set;

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

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

   

    public AdminRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminRequestDTO(String username, String password, String name, String email, String mobile,
			LocalDate dateOfBirth, String profileImage, Set<Long> jamatIds, boolean isSuperAdmin) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.dateOfBirth = dateOfBirth;
		this.profileImage = profileImage;
		this.jamatIds = jamatIds;
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

    public Set<Long> getJamatIds() {
        return jamatIds;
    }

    public void setJamatIds(Set<Long> jamatIds) {
        this.jamatIds = jamatIds;
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

    public void setSuperAdmin(boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }
}
