package com.postech.auth_service.service;

import com.postech.auth_service.dto.UserAuthenticated;
import com.postech.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(UserAuthenticated::new)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }
}
