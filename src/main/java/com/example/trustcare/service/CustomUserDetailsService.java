package com.example.trustcare.service;
import com.example.trustcare.enums.Role;
import com.example.trustcare.repository.AdminDAO;
import com.example.trustcare.repository.CaregiverDAO;
import com.example.trustcare.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;
    private final CaregiverDAO caregiverDAO;
    private final AdminDAO adminDAO;

    @Autowired
    public CustomUserDetailsService(UserDAO userDAO, CaregiverDAO caregiverDAO, AdminDAO adminDAO) {
        this.userDAO = userDAO;
        this.caregiverDAO = caregiverDAO;
        this.adminDAO = adminDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return Optional.ofNullable(userDAO.getUserByEmail(email))
                .map(u -> buildUser(u.getEmail(), u.getPassword(), Role.USER))
                .or(() -> Optional.ofNullable(caregiverDAO.getCaregiverByEmail(email))
                        .map(c -> buildUser(c.getEmail(), c.getPassword(), Role.CAREGIVER)))
                .or(() -> Optional.ofNullable(adminDAO.getAdminByEmail(email))
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