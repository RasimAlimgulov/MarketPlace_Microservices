package org.example.authorization_service.entity;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.authorization_service.Role;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document
public class User {
    @Id
    UUID id;
    String login;
    String password;
    Role role;
}
