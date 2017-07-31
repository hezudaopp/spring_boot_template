package com.youmayon.tutorial.service.impl;

import com.youmayon.tutorial.data.UserRepository;
import com.youmayon.tutorial.domain.User;
import com.youmayon.tutorial.exception.UserNotFoundException;
import com.youmayon.tutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jawinton on 25/06/2017.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User get(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username)
        );
    }

    @Override
    public User get(long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    @Override
    public List<User> list(boolean enabled) {
        return userRepository.findByEnabled(enabled);
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return get(username);
    }
}
