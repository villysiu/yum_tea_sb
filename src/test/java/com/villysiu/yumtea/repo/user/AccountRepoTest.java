package com.villysiu.yumtea.repo.user;


import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepoTest {
    @MockitoBean
    private SeedService seedService;
    @MockitoBean
    private StorageService storageService;
    @Autowired
    private AccountRepo accountRepo;

    @Test
    public void findByEmail() {
        Mockito.doNothing().when(seedService).init();
        Mockito.doNothing().when(storageService).init();

        Account account = new Account();
        account.setEmail("admin@test.com");
        account.setPassword("password");
        account.setNickname("testAdmin");
        accountRepo.save(account);

        Account fetchedAccount = accountRepo.findByEmail("admin@test.com").orElse(null);;
        assertNotNull(fetchedAccount);
        assertEquals(fetchedAccount.getEmail(), "admin@test.com");
        assertEquals(fetchedAccount.getNickname(), "testAdmin");
    }


}