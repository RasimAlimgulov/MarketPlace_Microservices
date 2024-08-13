package org.example.authentication_service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestData {
    private String login;
    private String password;
    private Role role;
}
