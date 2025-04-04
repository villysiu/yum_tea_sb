package com.villysiu.yumtea.repo.user;

import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepoTest {
    @MockitoBean
    private SeedService seedService;
    @MockitoBean
    private StorageService storageService;
    @Autowired
    private RoleRepo roleRepo;

    @Test
    public void findByName() {
        Mockito.doNothing().when(seedService).init();
        Mockito.doNothing().when(storageService).init();
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepo.save(role);

        Role fetchedRole = roleRepo.findByName("ROLE_USER").orElse(null);
        assertNotNull(fetchedRole);
        assertEquals("ROLE_USER", fetchedRole.getName());
    }
}