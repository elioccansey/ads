package com.eli.ads.user;

import com.eli.ads.user.auth.AuthenticationRequest;
import com.eli.ads.user.auth.AuthenticationResponse;
import com.eli.ads.user.auth.RegistrationRequest;

public interface UserService {
    User findUserById(Long id);
    User saveUser(RegistrationRequest registrationRequest);
    AuthenticationResponse register(RegistrationRequest registrationRequest);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    User getConnectedUser();
}
