package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.repo.user.AccountRepo;
import com.villysiu.yumtea.repo.user.RoleRepo;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@SpringBootTest
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private AccountRepo accountRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepo roleRepo;
    private SignupRequest testSignupRequest;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testSignupRequest = new SignupRequest("tester", "test@gg.com", "password1");
        testAccount = new Account();
        testAccount.setEmail("test@gg.com");
        testAccount.setNickname("tester");
        testAccount.setPassword(passwordEncoder.encode("password1"));
        testAccount.setRoles(Collections.singleton(null));


    }

    @Test
    void signup() {
//   no matter what Account object is passed into the save() method, this mock will return the specified value (testAccount)o matter what Account object is passed into the save() method, this mock will return the specified value (testAccount).
        when(accountRepo.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(accountRepo.save(any(Account.class))).thenReturn(testAccount);

        Long accountId = authenticationService.signup(testSignupRequest);

        assertEquals(accountId, testAccount.getId());
        verify(accountRepo, times(1)).findByEmail(any(String.class));
        verify(accountRepo, times(1)).save(testAccount);


    }

}