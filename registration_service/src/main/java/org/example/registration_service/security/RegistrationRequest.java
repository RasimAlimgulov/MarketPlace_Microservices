package org.example.registration_service.security;

import lombok.Getter;
import lombok.Setter;
import org.example.registration_service.Role;

@Getter
@Setter
public class RegistrationRequest {
    private String login;
    private String password;
    private Role role;
}
