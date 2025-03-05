package com.villysiu.yumtea.repo.user;


import com.villysiu.yumtea.models.user.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepoTest {

    @Autowired
    private AccountRepo accountRepo;

    @Test
    public void findByEmail() {
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