package org.example.authentication_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.authentication_service.entity.User;
import org.example.authentication_service.kafka.UserDTO;
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
@Log4j2
@Service
public class UserService implements UserDetailsService {
    private final ObjectMapper mapper;
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    @Autowired
    private final KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> factory;
    public UserService(ObjectMapper mapper, UserRepository repository, BCryptPasswordEncoder encoder, KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> factory) {
        this.mapper = mapper;
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
    public void handleUserCreated(String userJSON) throws JsonProcessingException {
       log.info("Сообщение получено "+userJSON);
        UserDTO userDTO=mapper.readValue(userJSON,UserDTO.class);
        User user=new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        repository.save(user);
        log.info("User сохранён в MongoDb");
    }
}
