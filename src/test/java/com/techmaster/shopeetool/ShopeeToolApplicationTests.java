package com.techmaster.shopeetool;

import com.techmaster.shopeetool.model.Role;
import com.techmaster.shopeetool.model.User;
import com.techmaster.shopeetool.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ShopeeToolApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    UserRepository userRepository;
    @Test
    public void testCreateUser(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String decodePassword = "123456";

        User newUser = new User("quangdz@gmail.com",passwordEncoder.encode(decodePassword));
        User savedUser = userRepository.save(newUser);
        Assert.notNull(savedUser);
        Assert.isTrue(savedUser.getId()>0);
    }
    //    @Test
//    public void changePasswordUser(){
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String decodePassword = "123456";
//        User user = userRepository.findByEmail("customer1@gmail.com").get();
//        user.setPassword(passwordEncoder.encode(decodePassword));
//        Assert.isTrue(user.getPassword().length() == 6);
//    }
    @Test
    public void testAssignRolesToUser(){
        Integer userId = 1;
        User user = userRepository.findById(userId).get();
        user.addRole(new Role(3));
        User updatedUser = userRepository.save(user);
        Assert.isTrue(updatedUser.getRoleSet().size() == 1);
    }
}
