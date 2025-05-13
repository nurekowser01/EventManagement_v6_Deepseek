package org.mkab.EventManagement.controller;

import java.util.List;

import org.mkab.EventManagement.dto.AdminRequestDTO;
import org.mkab.EventManagement.dto.AdminResponseDTO;
import org.mkab.EventManagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<AdminResponseDTO> create(@RequestBody AdminRequestDTO dto) {
        return ResponseEntity.ok(adminService.createAdmin(dto));
    }

    @GetMapping
    public List<AdminResponseDTO> getAll() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> update(@PathVariable Long id, @RequestBody AdminRequestDTO dto) {
        return ResponseEntity.ok(adminService.updateAdmin(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    // Exception handling: Return a response with an error message when admin is not found
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleAdminNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
    // Upload Admin Profile Image
    @PostMapping("/{id}/upload-profile-image")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long id,
                                                     @RequestParam("image") MultipartFile imageFile) {
        String imageUrl = adminService.uploadProfileImage(id, imageFile);
        return ResponseEntity.ok(imageUrl);
    }

    
}
