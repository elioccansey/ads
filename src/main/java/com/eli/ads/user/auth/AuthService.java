package com.eli.ads.user.auth;

import com.eli.ads.user.*;
import com.eli.ads.user.entity.User;
import com.eli.ads.user.exception.DuplicateUserException;
import com.eli.ads.user.security.JwtService;
import com.eli.ads.user.service.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        String username = registrationRequest.username();
        userRepository
                .findByUsername(username)
                .ifPresent( user -> {
                    throw new DuplicateUserException("User exists with username: " + user.getUsername());
                });

        User newUser = userMapper.toUser(registrationRequest);
        User savedUser = userRepository.save(newUser);
        String token = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);

    }
}
