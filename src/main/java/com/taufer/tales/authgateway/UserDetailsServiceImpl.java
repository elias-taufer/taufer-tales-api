package com.taufer.tales.authgateway;

import com.taufer.tales.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }
}