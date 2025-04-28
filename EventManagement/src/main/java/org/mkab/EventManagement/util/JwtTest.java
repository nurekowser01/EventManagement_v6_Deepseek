package org.mkab.EventManagement.util;

import org.mkab.EventManagement.util.JwtUtil;

public class JwtTest {

    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();

        // Test token generation
        String username = "testUser";
        String token = jwtUtil.generateToken(username);
        System.out.println("Generated Token: " + token);

        // Test token validation
        boolean isValid = jwtUtil.validateToken(token, username);
        System.out.println("Is Token Valid: " + isValid);

        // Test extracting username from the token
        String extractedUsername = jwtUtil.extractUsername(token);
        System.out.println("Extracted Username: " + extractedUsername);
    }
}
