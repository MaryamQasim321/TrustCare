package com.example.trustcare.services;

import com.example.trustcare.enums.Role;
import com.example.trustcare.repository.AdminRepository;
import com.example.trustcare.repository.CaregiverRepository;
import com.example.trustcare.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CaregiverRepository caregiverRepo;
    @Autowired
    private AdminRepository adminRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email)
                .map(u -> buildUser(u.getEmail(), u.getPassword(), Role.USER))
                .or(() -> caregiverRepo.findByEmail(email)
                        .map(c -> buildUser(c.getEmail(), c.getPassword(), Role.CAREGIVER)))
                .or(() -> adminRepo.findByEmail(email)
                        .map(a -> buildUser(a.getEmail(), a.getPassword(), Role.ADMIN)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    private UserDetails buildUser(String email, String password, Role role) {
        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                List.of(new SimpleGrantedAuthority(role.name()))
        );
    }
}

