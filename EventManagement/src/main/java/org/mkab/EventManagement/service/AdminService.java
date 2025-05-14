package org.mkab.EventManagement.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mkab.EventManagement.dto.AdminRequestDTO;
import org.mkab.EventManagement.dto.AdminResponseDTO;
import org.mkab.EventManagement.entity.Admin;
import org.mkab.EventManagement.entity.Jamat;
import org.mkab.EventManagement.repository.AdminRepository;
import org.mkab.EventManagement.repository.JamatRepository;
import org.mkab.EventManagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AdminResponseDTO createAdmin(AdminRequestDTO dto) {
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


//        if (dto.getJamatIds() != null && !dto.getJamatIds().isEmpty()) {
//            Set<Jamat> jamats = new HashSet<>(jamatRepo.findAllById(dto.getJamatIds()));
//            admin.setJamats(jamats);
//        } else {
//            admin.setJamats(new HashSet<>());
//        }

        Admin saved = adminRepo.save(admin);
        return mapToResponseDTO(saved);
    }

    // Update an existing Admin
    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO dto) {
        Admin admin = adminRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Check if the username is different and if it already exists
        if (!admin.getUsername().equals(dto.getUsername()) && adminRepo.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

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

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getJamatIds() != null && !dto.getJamatIds().isEmpty()) {
            Set<Jamat> jamats = new HashSet<>(jamatRepo.findAllById(dto.getJamatIds()));
            admin.setJamats(jamats);
        } else {
            admin.setJamats(new HashSet<>());
        }

        Admin saved = adminRepo.save(admin);
        return mapToResponseDTO(saved);
    }


    

    // Get all Admins
    public List<AdminResponseDTO> getAllAdmins() {
        return adminRepo.findAll()
            .stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    // Get Admin by ID
    public AdminResponseDTO getAdminById(Long id) {
        return adminRepo.findById(id)
            .map(this::mapToResponseDTO)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

        // Helper method to map Admin to AdminResponseDTO
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

//        dto.setRoles(admin.getRoles().stream()
//            .map(role -> new RoleDTO(role.getName()))  // Assuming RoleDTO has a constructor that takes a name
//            .collect(Collectors.toList()));
        return dto;
    }
    
 // Delete an Admin by ID
    public void deleteAdmin(Long id) {
        Admin admin = adminRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Optionally, you can add some validation here to check if this admin can be deleted.
        // For example, preventing deletion of the Super Admin.

        adminRepo.delete(admin);  // Delete the Admin from the repository
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
        if (!uploadPath.exists()) uploadPath.mkdirs();

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
