package com.github.alekseypetkun.TransportTicketApp.security.custom;

import com.github.alekseypetkun.TransportTicketApp.dao.impl.UserDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.exception.NotFoundUserException;
import com.github.alekseypetkun.TransportTicketApp.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements CustomUserDetailsService {

    private final UserDaoImpl userDao;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new NotFoundUserException(username);
        }

        JwtUser jwtUser = new JwtUser(user);
        log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
        return jwtUser;
    }

    @Override
    public UserDetails loadUserById(Long userId) {

        User user = userDao.searchById(userId);
        if (user == null) {
            throw new NotFoundUserException(userId);
        }

        JwtUser jwtUser = new JwtUser(user);
        log.info("IN loadUserByUsername - user with userId: {} successfully loaded", userId);
        return jwtUser;
    }
}
