package org.example.authentication_service.controller;

import lombok.extern.log4j.Log4j2;
import org.example.authentication_service.RequestData;
import org.example.authentication_service.entity.User;
import org.example.authentication_service.security.JwtUtil;
import org.example.authentication_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
@Log4j2
@RestController
public class AuthenticationController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    private UserService service;


    public AuthenticationController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, BCryptPasswordEncoder encoder) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody RequestData requestData) {
        try {
           Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestData.getLogin()
                    , requestData.getPassword()));

        UserDetails userDetails =(UserDetails) authentication.getPrincipal();// Получаем логин и пароль

        String jwtToken = jwtUtil.generateToken(userDetails); /// генерируем jwt токен
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());/// генерируем refresh токен

        ResponseData responseData = new ResponseData(); /// добавляем данные в объект и вызвращаем клиенту ключи
        responseData.setJwtToken(jwtToken);
        responseData.setRefreshToken(refreshToken);

        return ResponseEntity.ok(responseData);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect login or password");
        }catch (DisabledException t){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Disabled User");
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            log.info("Не прошла проверка жизни рефреш токена");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");

        }
        String username=jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = service.loadUserByUsername(username);

        if (userDetails==null){
            log.info("Юзер с таким именем не найден");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized client");
        }

        String newJwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(newJwtToken);
    }


    @PostMapping("/create")
    public User create(@RequestBody User user) {
        return service.createUser(user);
    }


}
