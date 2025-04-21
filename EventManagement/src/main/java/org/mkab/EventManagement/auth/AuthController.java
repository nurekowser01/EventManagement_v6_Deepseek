package org.mkab.EventManagement.auth;

import org.mkab.EventManagement.auth.AuthRequest;
import org.mkab.EventManagement.auth.AuthResponse;
import org.mkab.EventManagement.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()
                )
            );
            System.out.println("🔍 Loading AuthResponse password: " + request.getPassword());
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return new AuthResponse(token);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

}