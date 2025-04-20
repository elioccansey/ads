package com.eli.ads.user.auth;

import com.eli.ads.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
       @NotBlank String username,
       @NotBlank String password,
       @NotNull Role role
){
}
