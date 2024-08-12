package org.example.authorization_service.service;

import org.example.authorization_service.entity.User;
import org.example.authorization_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository repository;
    public UserDetails loadUserByUsername(String login) {
        User user= repository.findByLogin(login).get();
        return new org.springframework.security.core.userdetails.User(user.getLogin(),user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString())));
    }
}
