package org.example.registration_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.registration_service.entity.User;
import org.example.registration_service.kafka.UserDTO;
import org.example.registration_service.security.RegistrationRequest;
import org.example.registration_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/reg")
public class RegistrationController {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private KafkaTemplate<String,String > kafkaTemplate;

    @PostMapping("/registration")
    public ResponseEntity<?> registUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            log.info("Начинается регистрация");
            String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
            User user = new User();
            user.setLogin(registrationRequest.getLogin());
            user.setPassword(encodedPassword);
            user.setRole(registrationRequest.getRole());
            userDetailsService.createUser(user);
            log.info("Регистрация завершена и отправяем user в Kafka");
            UserDTO userDTO=new UserDTO();
            userDTO.setLogin(user.getLogin());
            userDTO.setPassword(user.getPassword());
            userDTO.setRole(user.getRole());
            String userJSON=mapper.writeValueAsString(userDTO);
            kafkaTemplate.send("user_created",userJSON);
            log.info("Сообщение отправлено");
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }
    @GetMapping ("/test")
    public String getWord(){
        return "adsadafdadsdasf";
    }



}