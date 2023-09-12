package com.example.level04.service;

import com.example.level04.dto.SignupRequestDto;
import com.example.level04.entity.User;
import com.example.level04.entity.UserRoleEnum;
import com.example.level04.jwt.JwtUtil;
import com.example.level04.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

//    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public User signup(SignupRequestDto requestDto){
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
//        String password = requestDto.getPassword();

        Optional<User> isValueUsername = userRepository.findByUsername(username);
        if(isValueUsername.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isAdmin()){
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username,password,role);
        return userRepository.save(user);
    }
}
