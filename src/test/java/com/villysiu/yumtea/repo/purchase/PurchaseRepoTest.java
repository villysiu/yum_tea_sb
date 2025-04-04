package com.villysiu.yumtea.repo.purchase;

import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.repo.user.AccountRepo;
import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class PurchaseRepoTest {
    @MockitoBean
    private SeedService seedService;
    @MockitoBean
    private StorageService storageService;
    @Autowired
    private PurchaseRepo purchaseRepo;
    @Autowired
    private AccountRepo accountRepo;

    private Account testAccount1;
    private Account testAccount2;


    @BeforeEach
    void setUp() {
        Mockito.doNothing().when(seedService).init();
        Mockito.doNothing().when(storageService).init();
        testAccount1 = new Account("admin1@test.com", "testAdmin", "password2");
        accountRepo.save(testAccount1);
        testAccount2 = new Account("admin2@test.com", "testAdmin", "password2");
        accountRepo.save(testAccount2);

        for(int i=0; i<5; i++) {
            Purchase testPurchase = new Purchase(testAccount1);
            purchaseRepo.save(testPurchase);
        }
        for(int i=0; i<2; i++) {
            Purchase testPurchase = new Purchase(testAccount2);
            purchaseRepo.save(testPurchase);
        }
    }

    @Test
    void findByAccountId() {

        List<Purchase> fetchedPurchaseByTestAccount1 = purchaseRepo.findByAccountId(testAccount1.getId(),
                Purchase.class);
        List<Purchase> fetchedPurchaseByTestAccount2 = purchaseRepo.findByAccountId(testAccount2.getId(),
                Purchase.class);

        assertNotNull(fetchedPurchaseByTestAccount1);
        assertEquals(5, fetchedPurchaseByTestAccount1.size());

        assertNotNull(fetchedPurchaseByTestAccount2);
        assertEquals(2, fetchedPurchaseByTestAccount2.size());
    }

    @Test
    void findByAccountIdAndPurchaseIdQuery() {
        List<Purchase> fetchedPurchaseByTestAccount1 = purchaseRepo.findByAccountId(testAccount1.getId(),
                Purchase.class);
        List<Purchase> fetchedPurchaseByTestAccount2 = purchaseRepo.findByAccountId(testAccount2.getId(),
                Purchase.class);

        Purchase testPurchaseBelongedToTestAccount1 =
                purchaseRepo.findByAccountIdAndPurchaseIdQuery(testAccount1.getId(),
                        fetchedPurchaseByTestAccount1.get(0).getId(),
                        Purchase.class).orElse(null);
        assertNotNull(testPurchaseBelongedToTestAccount1);

        Purchase testPurchaseNotBelongedToTestAccount1 =
                purchaseRepo.findByAccountIdAndPurchaseIdQuery(testAccount1.getId(),
                        fetchedPurchaseByTestAccount2.get(0).getId(),
                        Purchase.class).orElse(null);
        assertNull(testPurchaseNotBelongedToTestAccount1);

    }
}