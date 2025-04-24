package com.eli.ads.user.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RegistrationRequest(
       @NotBlank String username,
       @NotBlank String password,
       @NotNull Set<String> roles
){
}
