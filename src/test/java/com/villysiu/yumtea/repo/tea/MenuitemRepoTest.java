package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.dto.response.BestSellerDto;
import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.repo.user.AccountRepo;
import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageService;
import net.bytebuddy.matcher.FilterableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class MenuitemRepoTest {

    @MockitoBean
    private SeedService seedService;
    @MockitoBean
    private StorageService storageService;

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private MenuitemRepo menuitemRepo;
    @Autowired
    private PurchaseRepo purchaseRepo;
    @Autowired
    private PurchaseLineitemRepo purchaseLineitemRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private MilkRepo milkRepo;
    @Autowired
    private SizeRepo sizeRepo;

    private Category testCategory1;
    private Category testCategory2;

    private Milk testMilk;
    private Size testSize;
    private Sugar testSugar;
    private Temperature testTemperature;

    @BeforeEach
    void setUp() {

        Mockito.doNothing().when(seedService).init();
        Mockito.doNothing().when(storageService).init();

        testCategory1 = new Category("Beverages1");
        categoryRepo.save(testCategory1);

        testCategory2 = new Category("Beverages2");
        categoryRepo.save(testCategory2);

        testMilk = new Milk("testMilk");
        milkRepo.save(testMilk);
        testSize = new Size("testSize");
        sizeRepo.save(testSize);

        testSugar = Sugar.values()[0];
        testTemperature = Temperature.values()[0];

        for (int i = 0; i < 5; i++) {
            Menuitem menuitem = new Menuitem("Drink"+i, "IMG_0210.png", testCategory1, testMilk, testSugar, testTemperature, 6.0);

            menuitemRepo.save(menuitem);
        }
        for (int i = 0; i < 3; i++) {
            Menuitem menuitem = new Menuitem("Drink"+i, "IMG_0210.png", testCategory2, testMilk, testSugar, testTemperature, 6.0);
            menuitemRepo.save(menuitem);
        }
    }

    @Test
    void findByCategoryIdQuery() {

        List<Menuitem> fetchedMenuitems1 = menuitemRepo.findByCategoryIdQuery(testCategory1.getId());
        List<Menuitem> fetchedMenuitems2 = menuitemRepo.findByCategoryIdQuery(testCategory2.getId());

        assertEquals(5, fetchedMenuitems1.size());
        assertEquals(3, fetchedMenuitems2.size());

        for (Menuitem menuitem : fetchedMenuitems1) {
            assertEquals("Beverages1", menuitem.getCategory().getTitle());
        }
        for (Menuitem menuitem : fetchedMenuitems2) {
            assertEquals("Beverages2", menuitem.getCategory().getTitle());
        }
    }
    @Test
    void findBestSellers(){
        Account testAccount = new Account("admin@test.com", "testAdmin", "password");
        accountRepo.save(testAccount);
        Purchase testPurchase = new Purchase(testAccount);
        purchaseRepo.save(testPurchase);


        List<Menuitem> testMenuitems = menuitemRepo.findAll();


        // create 1 each item 2 and item 1
        PurchaseLineitem purchaseLineitem1 = new PurchaseLineitem(testPurchase, testMenuitems.get(0), testMilk, testSize);
        purchaseLineitemRepo.save(purchaseLineitem1);
        PurchaseLineitem purchaseLineitem2 = new PurchaseLineitem(testPurchase, testMenuitems.get(1), testMilk, testSize);
        purchaseLineitemRepo.save(purchaseLineitem2);

        // create 5  item 2
        PurchaseLineitem purchaseLineitem0 = new PurchaseLineitem(testPurchase,
                testMenuitems.get(2), testMilk, testSize);
        purchaseLineitem0.setQuantity(5);
        purchaseLineitemRepo.save(purchaseLineitem0);

        // create 3  item 3
        PurchaseLineitem purchaseLineitem4 = new PurchaseLineitem(testPurchase,
                testMenuitems.get(3), testMilk, testSize);
        purchaseLineitem4.setQuantity(3);
        purchaseLineitemRepo.save(purchaseLineitem4);

        // create 8  item 4
        PurchaseLineitem purchaseLineitem5 = new PurchaseLineitem(testPurchase,
                testMenuitems.get(4), testMilk, testSize);
        purchaseLineitem5.setQuantity(8);
        purchaseLineitemRepo.save(purchaseLineitem5);

        // create 6  item 3
        PurchaseLineitem purchaseLineitem6 = new PurchaseLineitem(testPurchase,
                testMenuitems.get(3), testMilk, testSize);
        purchaseLineitem6.setQuantity(6);
        purchaseLineitemRepo.save(purchaseLineitem6);
        // create 2 item 5
        PurchaseLineitem purchaseLineitem7 = new PurchaseLineitem(testPurchase,
                testMenuitems.get(5), testMilk, testSize);
        purchaseLineitem7.setQuantity(2);
        purchaseLineitemRepo.save(purchaseLineitem7);


        // create 1 each
        PurchaseLineitem purchaseLineitem8 = new PurchaseLineitem(testPurchase, testMenuitems.get(6), testMilk, testSize);
        purchaseLineitemRepo.save(purchaseLineitem8);
        PurchaseLineitem purchaseLineitem9 = new PurchaseLineitem(testPurchase, testMenuitems.get(7), testMilk, testSize);
        purchaseLineitemRepo.save(purchaseLineitem9);



        assertEquals(9, purchaseLineitemRepo.findAll().size());
        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> fetchedBestSellers = menuitemRepo.findBestSellers(pageable);

        // ensures only top 3 returned
        assertEquals(3, fetchedBestSellers.size());


        Object[] row0 = fetchedBestSellers.get(0);
        assertEquals( testMenuitems.get(3).getId(), (Long) row0[0]);
        assertEquals(9, (Long) row0[2]);

        Object[] row1 = fetchedBestSellers.get(1);
        assertEquals(testMenuitems.get(4).getId(), (Long) row1[0]);
        assertEquals(8, (Long) row1[2]);

        Object[] row2 = fetchedBestSellers.get(2);
        assertEquals(testMenuitems.get(2).getId(), (Long) row2[0]);
        assertEquals(5, (Long) row2[2]);

        // ensures list in descending order
        assertTrue((Long) row0[2] > (Long) row1[2]);
        assertTrue((Long) row1[2] > (Long) row2[2]);









//        Pageable pageable = PageRequest.of(0, 3);
//        List<Object[]> bestSellerMenuitems(Pageable pageable);
    }
}


