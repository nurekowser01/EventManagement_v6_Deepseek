package org.mkab.EventManagement;

import java.time.LocalDate;

//DataInitializer.java


import org.mkab.EventManagement.entity.Admin;
import org.mkab.EventManagement.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class RunnerAdmin {

 @Autowired
 private PasswordEncoder passwordEncoder;

 @Bean
 CommandLineRunner initAdmin(AdminRepository adminRepository) {
     return args -> {
         if (adminRepository.findByUsername("admin").isEmpty()) {
             Admin admin = new Admin();
             admin.setUsername("admin");
             admin.setPassword(passwordEncoder.encode("123")); // hashed
             admin.setName("Super Admin");
             admin.setEmail("admin@example.com");
             admin.setMobile("1234567890");
             admin.setDateOfBirth(LocalDate.parse("1990-01-01"));
             admin.setProfileImage(null); // optional
             admin.setSuperAdmin(true); 
             adminRepository.save(admin);

             System.out.println("✅ Super Admin user created: superadmin / admin123");
         } else {
             System.out.println("ℹ️ Super Admin already exists.");
         }
     };
 }
}

