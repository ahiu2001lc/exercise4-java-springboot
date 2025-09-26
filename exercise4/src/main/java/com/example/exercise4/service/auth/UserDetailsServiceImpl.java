package com.example.exercise4.service.auth;

import com.example.exercise4.entity.UserEntity;
import com.example.exercise4.exception.ExceptionMessages;
import com.example.exercise4.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
        UserEntity user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND + " " + username));

        boolean offActived = user.getEnabled() != null && user.getEnabled() == 0;
        if (offActived) {
            log.info(ExceptionMessages.NOT_ACTIVE);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        user.getUserRoles().stream()
                                .map(ur -> ur.getRole().getName())
                                .toArray(String[]::new)
                )
                .disabled(offActived)
                .build();
    }
}
