package com.graduatemanagement.service;

import com.graduatemanagement.dto.LoginRequest;
import com.graduatemanagement.dto.LoginResponse;
import com.graduatemanagement.dto.RegisterRequest;
import com.graduatemanagement.entity.User;
import com.graduatemanagement.repository.UserRepository;
import com.graduatemanagement.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtTokenProvider.generateToken(request.getUsername());
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

            return new LoginResponse(token, user.getUsername(), user.getRole(), user.getId());
        } catch (AuthenticationException e) {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole("USER");

        return userRepository.save(user);
    }
}
