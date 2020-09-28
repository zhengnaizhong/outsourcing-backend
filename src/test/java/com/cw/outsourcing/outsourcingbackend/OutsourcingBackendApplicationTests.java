package com.cw.outsourcing.outsourcingbackend;

import com.cw.outsourcing.outsourcingbackend.dao.repository.UserRepository;
import com.cw.outsourcing.outsourcingbackend.pojo.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class OutsourcingBackendApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserEntity userEntity = new UserEntity();
        userEntity.setNickName("郑乃中");
        userEntity.setUserName("zhengnaizhong");
        userEntity.setPassword(bCryptPasswordEncoder.encode("123456"));
        userEntity.setEmail("zhengnaizhong@cmii.chinamobile.com");
        userRepository.save(userEntity);
    }

}
