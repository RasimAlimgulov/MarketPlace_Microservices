package org.example.authentication_service.kafka;

import lombok.Getter;
import lombok.Setter;
import org.example.authentication_service.Role;

@Getter
@Setter
public class UserDTO {
    private long id;
    private String login;
    private String password;
    private Role role;
}
