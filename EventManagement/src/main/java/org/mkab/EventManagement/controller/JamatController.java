package org.mkab.EventManagement.controller;


import org.apache.coyote.BadRequestException;
import org.mkab.EventManagement.entity.Jamat;
import org.mkab.EventManagement.service.JamatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jamat")
public class JamatController {

    @Autowired
    private JamatService jamatService;

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public List<Jamat> getAllJamat() {
        return jamatService.getAllJamat();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Jamat> getJamatById(@PathVariable Long id) {
        return ResponseEntity.ok(jamatService.getJamatById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Jamat> createJamat(@RequestBody Jamat jamat) {
        try {
			return ResponseEntity.ok(jamatService.createJamat(jamat));
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Jamat> updateJamat(@PathVariable Long id, @RequestBody Jamat jamatDetails) {
        return ResponseEntity.ok(jamatService.updateJamat(id, jamatDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteJamat(@PathVariable Long id) {
        jamatService.deleteJamat(id);
        return ResponseEntity.ok().build();
    }
}