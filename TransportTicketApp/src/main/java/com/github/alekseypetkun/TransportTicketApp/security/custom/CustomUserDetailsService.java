package com.github.alekseypetkun.TransportTicketApp.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {

    UserDetails loadUserById(Long userId);
}
