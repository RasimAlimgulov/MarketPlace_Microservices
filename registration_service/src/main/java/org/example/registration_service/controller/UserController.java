package org.example.registration_service.controller;
import lombok.extern.log4j.Log4j2;
import org.example.registration_service.entity.User;
import org.example.registration_service.security.AuthRequest;
import org.example.registration_service.security.AuthResponse;
import org.example.registration_service.security.JwtUtil;
import org.example.registration_service.security.RegistrationRequest;
import org.example.registration_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
             User user = new User();
            user.setLogin(registrationRequest.getEmail());
            user.setPassword(encodedPassword);
            userDetailsService.createUser(user);

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
//        log.info("Вызвался метод логирования");
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword())
//            );
//            log.info("Успешно прошла процедура authentication");
//        } catch (Exception e) {
//            throw new Exception("Incorrect email or password", e);
//        }
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getLogin());
//        log.info("Получили UserDetails");
//        final String jwt = jwtUtil.generateToken(userDetails);
//        log.info("Сгенерировали токен");
//        return ResponseEntity.ok(new AuthResponse(jwt));
//    }




}