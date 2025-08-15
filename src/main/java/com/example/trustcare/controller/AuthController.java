package com.example.trustcare.controller;

import com.example.trustcare.dto.AuthRequest;
import com.example.trustcare.dto.AuthResponse;
import com.example.trustcare.model.Admin;
import com.example.trustcare.model.Caregiver;
import com.example.trustcare.model.User;
import com.example.trustcare.repository.AdminRepository;
import com.example.trustcare.repository.CaregiverRepository;
import com.example.trustcare.repository.UserRepository;
import com.example.trustcare.security.JWTUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired private JWTUtility jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;
    @Autowired private CaregiverRepository caregiverRepository;
    @Autowired private AdminRepository adminRepository;


    //testing api
    @GetMapping("/user")
    public ResponseEntity<String> testUserEndpoint() {
        return ResponseEntity.ok("This is a public endpoint for /auth/user");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(new AuthResponse(token, role));
    }

    @PostMapping("/signUp/user")
    public  ResponseEntity<?> signupUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/signUp/caregiver")
    public  ResponseEntity<?> signupCaregiver(@RequestBody Caregiver caregiver){
        caregiver.setPassword(passwordEncoder.encode(caregiver.getPassword()));
        caregiverRepository.save(caregiver);
        return ResponseEntity.ok("caregiver registered successfully");
    }

    @PostMapping("/signUp/admin")
    public  ResponseEntity<?> signupCaregiver(@RequestBody Admin admin){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        return ResponseEntity.ok("admin registered successfully");
    }

}