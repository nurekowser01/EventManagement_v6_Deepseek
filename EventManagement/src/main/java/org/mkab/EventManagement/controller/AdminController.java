package org.mkab.EventManagement.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.mkab.EventManagement.dto.AdminRequestDTO;
import org.mkab.EventManagement.dto.AdminResponseDTO;
import org.mkab.EventManagement.dto.PasswordChangeRequest;
import org.mkab.EventManagement.entity.Admin;
import org.mkab.EventManagement.repository.AdminRepository;
import org.mkab.EventManagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; // Correct import
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PostMapping
	public ResponseEntity<AdminResponseDTO> create(@RequestBody AdminRequestDTO dto) {
		String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.ok(adminService.createAdmin(dto, currentUsername));
	}

	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	@GetMapping
	public List<AdminResponseDTO> getAll() {
		return adminService.getAllAdmins();
	}

	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> get(@PathVariable Long id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin currentAdmin = adminRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        if (!currentAdmin.isSuperAdmin() && !currentAdmin.getId().equals(id)) {
            throw new AccessDeniedException("You can only view your own profile");
        }
        
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<AdminResponseDTO> update(@PathVariable Long id, @RequestBody AdminRequestDTO dto) {
		return ResponseEntity.ok(adminService.updateAdmin(id, dto));
	}

	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		adminService.deleteAdmin(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PostMapping("/{id}/upload-profile-image")
	public ResponseEntity<Map<String, String>> uploadProfileImage(@PathVariable Long id,
			@RequestParam("image") MultipartFile imageFile) {
		if (imageFile.isEmpty()) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", "No file uploaded."));
		}

		String contentType = imageFile.getContentType();
		if (!contentType.startsWith("image/")) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Only image files are allowed."));
		}

		long maxSize = 5 * 1024 * 1024;
		if (imageFile.getSize() > maxSize) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", "File size exceeds 5MB."));
		}

		try {
			String imageUrl = adminService.uploadProfileImage(id, imageFile);
			return ResponseEntity.ok(Collections.singletonMap("profileImage", imageUrl));

		} catch (Exception e) {
			return ResponseEntity.status(500).body(Collections.singletonMap("error", "Image upload failed."));
		}
	}
	
	@ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

}
