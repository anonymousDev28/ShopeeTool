package com.techmaster.shopeetool;

import com.github.javafaker.Faker;
import com.techmaster.shopeetool.model.Product;
import com.techmaster.shopeetool.model.Role;
import com.techmaster.shopeetool.model.User;
import com.techmaster.shopeetool.repository.ProductRepository;
import com.techmaster.shopeetool.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
class ShopeeToolApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
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
    @Test
    void testMockDataProduct(){
        String[] categoryArr = {"Men Clothes",
                                "Mobile & Gadgets",
                                "Consumer Electronics",
                                "Computer & Accessories"};
        String[] brandArr = {
                "Zara",
                "Aviano",
                "pattern",
                "guzado",
                "coolmate"
        };
        String[] shopDistributor = {"Hà nội","Hồ Chí Minh","Đà Nẵng","Thái Nguyên"};
        Random random = new Random();
        Faker faker = new Faker();
        for (int i = 0; i < 50; i++) {
            productRepository.save(Product.builder()
                    .category(categoryArr[0])
                    .brand(brandArr[random.nextInt(brandArr.length)])
                    .name(faker.commerce().productName())
                    .quantity(random.nextInt(100))
                    .shopType("Shopee Mall")
                    .shippingOption("Giao hàng nhanh")
                    .price(random.nextInt(20000000 - 1000000)+1000000)
                    .shopDistributor(shopDistributor[random.nextInt(shopDistributor.length)])
                    .promotion((random.nextInt(30-15)+15)+"%")
                    .build());
        }
    }
}
