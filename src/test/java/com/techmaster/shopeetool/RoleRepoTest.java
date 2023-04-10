package com.techmaster.shopeetool;

import com.techmaster.shopeetool.model.Role;
import com.techmaster.shopeetool.repository.RoleRepository;
import com.techmaster.shopeetool.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepoTest {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateRole(){
        Role admin = new Role("ROLE_ADMIN");
        Role editor = new Role("ROLE_EDITOR");
        Role customer = new Role("ROLE_CUSTOMER");

        roleRepository.saveAll(List.of(admin,editor,customer));
        Assert.isTrue(roleRepository.count() == 3);
    }
    @Test
    public void testRoles(){
        List<Role> roles = roleRepository.findAll();
        Assert.isTrue(roles.size() > 0);
        roles.forEach(System.out::println);
    }

}
