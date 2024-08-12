package org.example.authorization_service.repository;

import org.example.authorization_service.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    Optional<User> findByLogin(String login);
}
