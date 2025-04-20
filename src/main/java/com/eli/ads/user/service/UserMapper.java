package com.eli.ads.user.service;

import com.eli.ads.user.entity.User;
import com.eli.ads.user.auth.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toUser(RegistrationRequest registrationRequest){
        return new User (
                null,
                registrationRequest.username(),
                passwordEncoder.encode(registrationRequest.password()),
                registrationRequest.role()
        );
    }

}
