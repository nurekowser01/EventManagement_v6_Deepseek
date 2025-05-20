package org.mkab.EventManagement.auth;

import java.time.LocalDateTime;

import org.mkab.EventManagement.auth.AuthRequest;
import org.mkab.EventManagement.auth.AuthResponse;
import org.mkab.EventManagement.entity.Admin;
import org.mkab.EventManagement.repository.AdminRepository;
import org.mkab.EventManagement.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
	public AuthResponse login(@RequestBody AuthRequest request, HttpServletRequest servletRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String token = jwtUtil.generateToken(userDetails.getUsername());

			// ✅ Update login time and IP
			Admin admin = adminRepository.findByUsername(request.getUsername())
					.orElseThrow(() -> new RuntimeException("Admin not found"));

			admin.setLoginAttempts(0);
			admin.setLastLoginAt(LocalDateTime.now());
			admin.setLastLoginIp(getClientIp(servletRequest));
			admin.setUpdatedBy("system"); // or use authenticated user if available
			admin.setUpdatedAt(LocalDateTime.now());
			adminRepository.save(admin);

			return new AuthResponse(token);

		} catch (AuthenticationException e) {
			adminRepository.findByUsername(request.getUsername()).ifPresent(admin -> {
				int attempts = admin.getLoginAttempts() + 1;
				admin.setLoginAttempts(attempts);

				// Optionally lock account after N attempts
				if (attempts >= 5) {
					admin.setIsActive(false); // optional field
				}

				admin.setUpdatedBy("system");
				admin.setUpdatedAt(LocalDateTime.now());
				adminRepository.save(admin);
			});

			throw new RuntimeException("Invalid username or password");
		}

	}

	private String getClientIp(HttpServletRequest request) {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0]; // in case of multiple IPs
	}

}