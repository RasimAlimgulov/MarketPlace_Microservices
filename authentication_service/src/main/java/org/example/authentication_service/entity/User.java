package org.example.authentication_service.entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.authentication_service.Role;
import org.springframework.data.annotation.Id;
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
