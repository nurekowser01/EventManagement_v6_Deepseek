package org.mkab.EventManagement.Security;

//CustomUserDetailsService.java

import org.mkab.EventManagement.entity.*;
import org.mkab.EventManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

 @Autowired
 private AdminRepository adminRepository;

 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     Admin admin = adminRepository.findByUsername(username)
             .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
     System.out.println("🔍 Loading user: " + username);
     return new User(admin.getUsername(), admin.getPassword(), Collections.emptyList());
 }

}
