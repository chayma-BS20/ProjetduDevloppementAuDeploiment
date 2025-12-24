package org.example.security;

import org.example.entities.Role;
import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(emailOrUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + emailOrUsername));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(getAuthorities(user))
                .build();
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        if (user.getRole() == null || user.getRole().getTitle() == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getTitle().toUpperCase())
        );
    }
}
