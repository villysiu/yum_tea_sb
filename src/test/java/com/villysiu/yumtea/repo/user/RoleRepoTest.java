package com.villysiu.yumtea.repo.user;

import com.villysiu.yumtea.models.user.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepoTest {
    @Autowired
    private RoleRepo roleRepo;

    @Test
    public void findByName() {
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepo.save(role);

        Role fetchedRole = roleRepo.findByName("ROLE_USER").orElse(null);;
        assertNotNull(fetchedRole);
        assertEquals("ROLE_USER", fetchedRole.getName());
    }
}