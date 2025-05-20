package org.mkab.EventManagement.auth;

import java.time.LocalDateTime;

import org.mkab.EventManagement.dto.ErrorResponse;
import org.mkab.EventManagement.entity.Admin;
import org.mkab.EventManagement.repository.AdminRepository;
import org.mkab.EventManagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
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
	public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletRequest servletRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String token = jwtUtil.generateToken(userDetails.getUsername());

			Admin admin = adminRepository.findByUsername(request.getUsername())
					.orElseThrow(() -> new RuntimeException("Admin not found"));

			admin.setLoginAttempts(0);
			admin.setLastLoginAt(LocalDateTime.now());
			admin.setLastLoginIp(getClientIp(servletRequest));
			admin.setUpdatedBy("system");
			admin.setUpdatedAt(LocalDateTime.now());
			adminRepository.save(admin);

			return ResponseEntity.ok(new AuthResponse(token));

		} catch (DisabledException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ErrorResponse("Your account is disabled. Please contact the administrator."));
		} catch (AuthenticationException e) {
			adminRepository.findByUsername(request.getUsername()).ifPresent(admin -> {
				int attempts = admin.getLoginAttempts() + 1;
				admin.setLoginAttempts(attempts);

				if (attempts >= 5) {
					admin.setIsActive(false);
				}

				admin.setUpdatedBy("system");
				admin.setUpdatedAt(LocalDateTime.now());
				adminRepository.save(admin);
			});

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ErrorResponse("Invalid username or password."));
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