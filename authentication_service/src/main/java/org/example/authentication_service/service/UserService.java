package org.example.authentication_service.service;

import org.example.authentication_service.entity.User;
import org.example.authentication_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository repository;
    @Autowired
    BCryptPasswordEncoder encoder;
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
}
