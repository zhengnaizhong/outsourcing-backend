package com.cw.outsourcing.outsourcingbackend.service.impl;

import com.cw.outsourcing.outsourcingbackend.dao.repository.UserRepository;
import com.cw.outsourcing.outsourcingbackend.pojo.entity.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserName(userName);
        if (!userEntityOptional.isPresent()) {
            throw new UsernameNotFoundException("user not found");
        }
        UserEntity userEntity = userEntityOptional.get();
        return new User(userEntity.getUserName(), userEntity.getPassword(),
                userEntity.getEnabled().equals(1), true, true, true,
                new ArrayList<>());
    }

}
