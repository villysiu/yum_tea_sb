package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.repo.user.AccountRepo;
import com.villysiu.yumtea.repo.user.RoleRepo;
import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageService;
import com.villysiu.yumtea.service.user.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.HashSet;
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
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepo.save(role);

        testSignupRequest = new SignupRequest("tester", "test@gg.com", "password1");
        testAccount = new Account();
        testAccount.setEmail("test@gg.com");
        testAccount.setNickname("tester");
        testAccount.setPassword(passwordEncoder.encode("password1"));

        HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        testAccount.setRoles(roles);
//        System.out.println("logging testAccount "+ testAccount);


    }

    @Test
    void signup() {
//   no matter what Account object is passed into the save() method, this mock will return the specified value (testAccount)o matter what Account object is passed into the save() method, this mock will return the specified value (testAccount).
        when(accountRepo.existsByEmail(any(String.class))).thenReturn(false);

        when(accountRepo.save(any(Account.class))).thenReturn(testAccount);

        Long accountId = authenticationService.signup(testSignupRequest);

        assertEquals(accountId, testAccount.getId());
        verify(accountRepo, times(1)).existsByEmail(any(String.class));
        verify(accountRepo, times(1)).save(testAccount);


    }

}