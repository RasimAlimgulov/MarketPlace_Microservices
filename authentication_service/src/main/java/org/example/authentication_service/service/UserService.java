package org.example.authentication_service.service;

import org.example.authentication_service.entity.User;
import org.example.authentication_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    @Autowired
    private final KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, User>> factory;
    public UserService(UserRepository repository, BCryptPasswordEncoder encoder, KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, User>> factory) {
        this.repository = repository;
        this.encoder = encoder;
        this.factory = factory;
    }

    public UserDetails loadUserByUsername(String login) {
        User user= repository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login));
        return new org.springframework.security.core.userdetails.User(user.getLogin(),user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString())));
    }
    public User createUser(User user){
        String password=user.getPassword();
        user.setPassword(encoder.encode(password));
       return repository.save(user);
    }
    @KafkaListener(topics = "user_created",groupId = "my_id")
    public void handleUserCreated(User user){
        repository.save(user);
    }
}
