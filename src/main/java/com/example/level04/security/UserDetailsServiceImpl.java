package com.example.level04.security;

import com.example.level04.entity.User;
import com.example.level04.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j(topic = "UserDetailsSeviceImpl")
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.info(username);
        User user = userRepository.findByUsername(username).orElseThrow(()->

                new UsernameNotFoundException(
                        "not found " + username
                )
        );
        return new UserDetailsImpl(user);
    }
}
