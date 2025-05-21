package org.mkab.EventManagement.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mkab.EventManagement.dto.AdminRequestDTO;
import org.mkab.EventManagement.dto.AdminResponseDTO;
import org.mkab.EventManagement.entity.Admin;
import org.mkab.EventManagement.entity.Role;
import org.mkab.EventManagement.model.enums.RoleType;
import org.mkab.EventManagement.repository.AdminRepository;
import org.mkab.EventManagement.repository.JamatRepository;
import org.mkab.EventManagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private JamatRepository jamatRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Create a new Admin
	public AdminResponseDTO createAdmin(AdminRequestDTO dto, String currentUsername) {
		if (adminRepo.existsByUsername(dto.getUsername())) {
			throw new RuntimeException("Username already exists");
		}

		Admin admin = new Admin();
		admin.setUsername(dto.getUsername());
		admin.setPassword(passwordEncoder.encode(dto.getPassword()));
		admin.setName(dto.getName());
		admin.setEmail(dto.getEmail());
		admin.setMobile(dto.getMobile());
		admin.setDateOfBirth(dto.getDateOfBirth());
		admin.setProfileImage(dto.getProfileImage());
		admin.setSuperAdmin(dto.isSuperAdmin());
		admin.setIsActive(dto.getIsActive());
		admin.setNotes(dto.getNotes());
		admin.setLastLoginAt(dto.getLastLoginAt());
		admin.setLastLoginIp(dto.getLastLoginIp());
		admin.setCreatedAt(dto.getCreatedAt());
		admin.setUpdatedAt(dto.getUpdatedAt());

		admin.setCreatedBy(currentUsername);
		admin.setUpdatedBy(currentUsername);

		admin.setLoginAttempts(0);
		admin.setLastPasswordChangeAt(LocalDateTime.now()); // Optional: or null if never changed

		// Assign roles from dto
	    Set<Role> roles = new HashSet<>();
	    if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
	        roles = dto.getRoles().stream()
	                .map(roleType -> roleRepo.findByType(roleType)
	                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleType)))
	                .collect(Collectors.toSet());
	    }
	    admin.setRoles(roles);
	    
		Admin saved = adminRepo.save(admin);
		return mapToResponseDTO(saved);
	}

	// Update an existing Admin
	public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO dto) {
		Admin admin = adminRepo.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
		
		String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// Check if the username is different and if it already exists
		if (!admin.getUsername().equals(dto.getUsername()) && adminRepo.existsByUsername(dto.getUsername())) {
			throw new RuntimeException("Username already exists");
		}

		admin.setUsername(dto.getUsername());
		
		admin.setName(dto.getName());
		admin.setEmail(dto.getEmail());
		admin.setMobile(dto.getMobile());
		admin.setDateOfBirth(dto.getDateOfBirth());
		admin.setProfileImage(dto.getProfileImage());
		admin.setSuperAdmin(dto.isSuperAdmin());
		admin.setIsActive(dto.getIsActive());
		admin.setNotes(dto.getNotes());
//        admin.setLastLoginAt(dto.getLastLoginAt());
//        admin.setLastLoginIp(dto.getLastLoginIp());
		admin.setCreatedAt(dto.getCreatedAt());
		admin.setUpdatedAt(dto.getUpdatedAt());

		admin.setCreatedBy(dto.getCreatedBy());
		admin.setUpdatedBy(currentUsername);

		if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
			admin.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		// Update roles
	    Set<Role> roles = new HashSet<>();
	    if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
	        roles = dto.getRoles().stream()
	                .map(roleType -> roleRepo.findByType(roleType)
	                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleType)))
	                .collect(Collectors.toSet());
	    }
	    admin.setRoles(roles);

		Admin saved = adminRepo.save(admin);
		return mapToResponseDTO(saved);
	}

	// Get all Admins
	public List<AdminResponseDTO> getAllAdmins() {
		return adminRepo.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}

	// Get Admin by ID
	public AdminResponseDTO getAdminById(Long id) {
		return adminRepo.findById(id).map(this::mapToResponseDTO)
				.orElseThrow(() -> new RuntimeException("Admin not found"));
	}

	private AdminResponseDTO mapToResponseDTO(Admin admin) {
		AdminResponseDTO dto = new AdminResponseDTO();
		dto.setId(admin.getId());
		dto.setUsername(admin.getUsername());
		dto.setName(admin.getName());
		dto.setEmail(admin.getEmail());
		dto.setMobile(admin.getMobile());
		dto.setDateOfBirth(admin.getDateOfBirth());
		dto.setProfileImage(admin.getProfileImage());
		dto.setSuperAdmin(admin.isSuperAdmin());
		dto.setIsActive(admin.getIsActive());
		dto.setNotes(admin.getNotes());
		dto.setLastLoginAt(admin.getLastLoginAt());
		dto.setLastLoginIp(admin.getLastLoginIp());
		dto.setCreatedAt(admin.getCreatedAt());
		dto.setUpdatedAt(admin.getUpdatedAt());
		dto.setCreatedBy(admin.getCreatedBy());
		dto.setUpdatedBy(admin.getUpdatedBy());
		// Convert roles to RoleType Set (or your preferred format)
	    Set<RoleType> roleTypes = admin.getRoles().stream()
	                                  .map(role -> role.getType())
	                                  .collect(Collectors.toSet());
	    dto.setRoles(roleTypes);
	    
		return dto;
	}

	// Delete an Admin by ID
	public void deleteAdmin(Long id) {
		Admin admin = adminRepo.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));

		// Optionally, you can add some validation here to check if this admin can be
		// deleted.
		// For example, preventing deletion of the Super Admin.

		adminRepo.delete(admin); // Delete the Admin from the repository
	}

	// Upload Profile Image
	public String uploadProfileImage(Long adminId, MultipartFile imageFile) {
		Optional<Admin> adminOpt = adminRepo.findById(adminId);
		if (adminOpt.isEmpty()) {
			throw new RuntimeException("Admin not found");
		}

		Admin admin = adminOpt.get();

		String uploadDir = "uploads/admins/";
		String fileName = adminId.toString();
		File uploadPath = new File(uploadDir);
		if (!uploadPath.exists())
			uploadPath.mkdirs();

		try {
			Path filePath = Paths.get(uploadDir, fileName);
			Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			String imageUrl = "/uploads/admins/" + fileName;

			admin.setProfileImage(imageUrl);
			adminRepo.save(admin);
			return imageUrl;
		} catch (IOException e) {
			throw new RuntimeException("Failed to upload image", e);
		}
	}

}
