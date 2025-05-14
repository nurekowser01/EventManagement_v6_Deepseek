package org.mkab.EventManagement.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
            return ResponseEntity.ok(Collections.singletonMap("imageUrl", imageUrl));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Image upload failed."));
        }
    }


    
}
