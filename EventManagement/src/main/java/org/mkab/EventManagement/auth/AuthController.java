package org.mkab.EventManagement.auth;

import java.time.LocalDateTime;

import org.mkab.EventManagement.entity.Admin;
import org.mkab.EventManagement.repository.AdminRepository;
import org.mkab.EventManagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletRequest servletRequest) {

        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));

        if (!Boolean.TRUE.equals(admin.getIsActive())) {
            throw new DisabledException("Your account is disabled. Please contact the administrator.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            String token = jwtUtil.generateToken(authentication.getName());

            // Reset login attempts, update login info on successful login
            admin.setLoginAttempts(0);
            admin.setLastLoginAt(LocalDateTime.now());
            admin.setLastLoginIp(getClientIp(servletRequest));
            admin.setUpdatedBy("system");
            admin.setUpdatedAt(LocalDateTime.now());
            
            adminRepository.save(admin);

            return ResponseEntity.ok(new AuthResponse(token, admin.getId()));

        } catch (BadCredentialsException ex) {
            // Increment login attempts
            int attempts = admin.getLoginAttempts() + 1;
            admin.setLoginAttempts(attempts);

//             Disable admin if attempts reach 5
            if (attempts >= 5) {
                admin.setIsActive(false);
            }

            admin.setUpdatedBy("system");
            admin.setUpdatedAt(LocalDateTime.now());
            adminRepository.save(admin);

            // Throw again to be handled by global exception handler for error formatting
            throw new BadCredentialsException("Invalid username or password.");
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}