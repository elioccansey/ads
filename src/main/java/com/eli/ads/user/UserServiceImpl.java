package com.eli.ads.user;

import com.eli.ads.user.auth.AuthenticationRequest;
import com.eli.ads.user.auth.AuthenticationResponse;
import com.eli.ads.user.auth.RegistrationRequest;
import com.eli.ads.user.auth.security.JwtService;
import com.eli.ads.user.exception.DuplicateUserException;
import com.eli.ads.user.role.Role;
import com.eli.ads.user.role.RoleEnum;
import com.eli.ads.user.role.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        User savedUser = this.saveUser(registrationRequest);
        String token = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(token);
    }

    @Override
    public User saveUser(RegistrationRequest registrationRequest) {
        String username = registrationRequest.username();
        userRepository
                .findByUsername(username)
                .ifPresent(user -> {
                    throw new DuplicateUserException("User exists with username: " + user.getUsername());
                });

        Set<Role> roles = registrationRequest
                .roles().stream()
                .map(role -> roleRepository
                        .findByName(RoleEnum.valueOf(role))
                        .orElseThrow(() -> new EntityNotFoundException("No role found with name " + role))
                ).collect(Collectors.toSet());


        User newUser = new User(
                null,
                registrationRequest.username(),
                passwordEncoder.encode(registrationRequest.password()),
                roles
        );

        newUser.setRoles(roles);
        return userRepository.save(newUser);

    }

    @Override
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

    @Override
    public User getConnectedUser(){
        return(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("No user found with id :" + id) );
    }
}
