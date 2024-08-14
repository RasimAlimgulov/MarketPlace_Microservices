package org.example.authentication_service.controller;

import org.example.authentication_service.RequestData;
import org.example.authentication_service.entity.User;
import org.example.authentication_service.security.JwtUtil;
import org.example.authentication_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService service;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody RequestData requestData)  {
           try{ authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestData.getLogin()
           ,requestData.getPassword()));}
           catch (BadCredentialsException e){
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect login or password");
           }
        UserDetails userDetails=service.loadUserByUsername(requestData.getLogin());
           String  jwtToken=jwtUtil.generateToken(userDetails);
           return ResponseEntity.ok(jwtToken);
    }
    @PostMapping("/create")
    public User create(@RequestBody User user){
        return service.createUser(user);
    }
}
