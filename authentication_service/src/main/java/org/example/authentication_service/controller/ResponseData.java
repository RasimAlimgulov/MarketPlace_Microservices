package org.example.authentication_service.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PathVariable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseData {
    String jwtToken;
    String refreshToken;
}
