package org.mkab.EventManagement.Security;

// CustomUserDetailsService.java

import org.mkab.EventManagement.entity.*;
import org.mkab.EventManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);

        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Admin not found for username: {}", username);
                    return new UsernameNotFoundException("Admin not found");
                });

        if (!admin.getIsActive()) {
            logger.warn("Login attempt for disabled admin: {}", username);
            throw new org.springframework.security.authentication.DisabledException("Your account is disabled.");
        }

        return new CustomUserDetails(admin);
    }



}
