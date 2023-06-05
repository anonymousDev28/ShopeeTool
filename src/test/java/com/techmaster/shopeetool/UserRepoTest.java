package com.techmaster.shopeetool;


import com.techmaster.shopeetool.model.Role;
import com.techmaster.shopeetool.model.User;
import com.techmaster.shopeetool.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Base64;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepoTest {
    @Autowired
    UserRepository userRepository;
    @Test
    public void testCreateUser(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String decodePassword = "Qdev5323";
// = new User("quang5320",passwordEncoder.encode(decodePassword));
        userRepository.save(User.builder().email("huaquang101").password(passwordEncoder.encode("Qdev5323")).shopeePassword(Base64.getEncoder().encodeToString("Qdev5323".getBytes())).build());
//        User savedUser = userRepository.save(newUser);
//        Assert.notNull(savedUser);
//        Assert.isTrue(savedUser.getId()>0);

//        User dummy = userRepository.findByEmail("quang5320").get();
//        dummy
//                .setShopeePassword(Base64.getEncoder()
//                .encodeToString("Qdev5323".getBytes()));
//        userRepository.save(dummy);
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
        Integer userId = 10;
        User user = userRepository.findById(userId).get();
        user.addRole(new Role(3));
        user.addRole(new Role(2));
        User updatedUser = userRepository.save(user);
        Assert.isTrue(updatedUser.getRoleSet().size() == 2);
    }
}
