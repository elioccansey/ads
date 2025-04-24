package com.eli.ads.user.auth;

import com.eli.ads.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
 class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse register(
             @RequestBody RegistrationRequest registrationRequest
    ){
        return userService.register(registrationRequest);
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ){
        return userService.authenticate(authenticationRequest);
    }

}
