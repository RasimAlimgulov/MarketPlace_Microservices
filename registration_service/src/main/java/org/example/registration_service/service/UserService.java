package org.example.registration_service.service;

import jakarta.transaction.Transactional;
import org.example.registration_service.entity.User;
import org.example.registration_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository repository;

    public Optional<User> getUserByLogin(String login) {
        return repository.findByLogin(login);
    }
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = getUserByLogin(login).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + login);
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), new ArrayList<>() {
        });
    }

    public void createUser(User user) {
        repository.save(user);
    }


}
