package org.mkab.EventManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.relation.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-many with Jamat
    @ManyToMany
    @JoinTable(
        name = "admin_jamats",
        joinColumns = @JoinColumn(name = "admin_id"),
        inverseJoinColumns = @JoinColumn(name = "jamats_id")
    )
    private Set<Jamat> jamats = new HashSet<>();

    private String username;
    private String password;
    private String name;
    private String email;
    public Admin(Long id, String username, String password, String name, String email, String phone,
			LocalDate dateOfBirth, String profileImage, Set<Jamat> jamats, Set<Role> roles, boolean isSuperAdmin) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.dateOfBirth = dateOfBirth;
		this.profileImage = profileImage;
		this.jamats = jamats;
		this.roles = roles;
		this.isSuperAdmin = isSuperAdmin;
	}

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Set<Jamat> getJamats() {
		return jamats;
	}

	public void setJamats(Set<Jamat> jamats) {
		this.jamats = jamats;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	private String phone;
    private LocalDate dateOfBirth;
    private String profileImage; // URL or base64 string

   
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private boolean isSuperAdmin;

    // Getters and Setters
}
