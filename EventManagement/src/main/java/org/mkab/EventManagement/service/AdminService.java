package org.mkab.EventManagement.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mkab.EventManagement.dto.AdminRequestDTO;
import org.mkab.EventManagement.dto.AdminResponseDTO;
import org.mkab.EventManagement.entity.Admin;
import org.mkab.EventManagement.entity.Jamat;
import org.mkab.EventManagement.repository.AdminRepository;
import org.mkab.EventManagement.repository.JamatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private JamatRepository jamatRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminResponseDTO createAdmin(AdminRequestDTO dto) {
        if (adminRepo.existsByUsername(dto.username)) {
            throw new RuntimeException("Username already exists");
        }

        Admin admin = new Admin();
        admin.setUsername(dto.username);
        admin.setPassword(passwordEncoder.encode(dto.password));
        admin.setName(dto.name);
        admin.setEmail(dto.email);
        admin.setPhone(dto.phone);
        admin.setDateOfBirth(dto.dateOfBirth);
        admin.setProfileImage(dto.profileImage);
        admin.setRoles(dto.roles);
        admin.setSuperAdmin(dto.isSuperAdmin);

        Set<Jamat> jamats = new HashSet<>(jamatRepo.findAllById(dto.jamatIds));
        admin.setJamats(jamats);

        Admin saved = adminRepo.save(admin);
        return mapToResponseDTO(saved);
    }

    public List<AdminResponseDTO> getAllAdmins() {
        return adminRepo.findAll()
            .stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    public AdminResponseDTO getAdminById(Long id) {
        return adminRepo.findById(id)
            .map(this::mapToResponseDTO)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO dto) {
        Admin admin = adminRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setName(dto.name);
        admin.setEmail(dto.email);
        admin.setPhone(dto.phone);
        admin.setDateOfBirth(dto.dateOfBirth);
        admin.setProfileImage(dto.profileImage);
        admin.setRoles(dto.roles);
        admin.setSuperAdmin(dto.isSuperAdmin);

        if (dto.password != null && !dto.password.isEmpty()) {
            admin.setPassword(passwordEncoder.encode(dto.password));
        }

        Set<Jamat> jamats = new HashSet<>(jamatRepo.findAllById(dto.jamatIds));
        admin.setJamats(jamats);

        return mapToResponseDTO(adminRepo.save(admin));
    }

    public void deleteAdmin(Long id) {
        adminRepo.deleteById(id);
    }

    private AdminResponseDTO mapToResponseDTO(Admin admin) {
        AdminResponseDTO dto = new AdminResponseDTO();
        dto.id = admin.getId();
        dto.username = admin.getUsername();
        dto.name = admin.getName();
        dto.email = admin.getEmail();
        dto.phone = admin.getPhone();
        dto.dateOfBirth = admin.getDateOfBirth();
        dto.profileImage = admin.getProfileImage();
        dto.roles = admin.getRoles();
        dto.isSuperAdmin = admin.isSuperAdmin();
        dto.jamatNames = admin.getJamats().stream()
            .map(Jamat::getName)
            .collect(Collectors.toSet());
        return dto;
    }
}
