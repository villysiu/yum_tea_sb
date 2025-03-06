package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.dto.response.BestSellerDto;
import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.tea.Category;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.repo.user.AccountRepo;
import net.bytebuddy.matcher.FilterableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class MenuitemRepoTest {

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


    @BeforeEach
    void setUp() {
        testCategory1 = new Category("Beverages1");
        categoryRepo.save(testCategory1);

        testCategory2 = new Category("Beverages2");
        categoryRepo.save(testCategory2);

        testMilk = new Milk("testMilk");
        milkRepo.save(testMilk);
        testSize = new Size("testSize");
        sizeRepo.save(testSize);

        for (int i = 0; i < 5; i++) {
            Menuitem menuitem = new Menuitem("Coffee1", testCategory1, testMilk);
            menuitemRepo.save(menuitem);
        }
        for (int i = 0; i < 3; i++) {
            Menuitem menuitem = new Menuitem("Coffee3", testCategory2, testMilk);
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

        List<Long> quantities = Arrays.asList(5L, 3L, 2L);
        List<Integer> menuitemIds = Arrays.asList(1, 7, 3);

        for(int i = 0; i < quantities.get(0); i++){
            PurchaseLineitem purchaseLineitem = new PurchaseLineitem(testPurchase,
                    testMenuitems.get(menuitemIds.get(0)), testMilk, testSize);
            purchaseLineitemRepo.save(purchaseLineitem);
        }

        for(int i = 0; i < quantities.get(1); i++){
            PurchaseLineitem purchaseLineitem = new PurchaseLineitem(testPurchase,
                    testMenuitems.get(menuitemIds.get(1)), testMilk, testSize);
            purchaseLineitemRepo.save(purchaseLineitem);
        }
        for(int i = 0; i < quantities.get(2); i++){
            PurchaseLineitem purchaseLineitem = new PurchaseLineitem(testPurchase,
                    testMenuitems.get(menuitemIds.get(2)), testMilk, testSize);
            purchaseLineitemRepo.save(purchaseLineitem);
        }
        PurchaseLineitem purchaseLineitem1 = new PurchaseLineitem(testPurchase, testMenuitems.get(4), testMilk, testSize);
        purchaseLineitemRepo.save(purchaseLineitem1);
        PurchaseLineitem purchaseLineitem2 = new PurchaseLineitem(testPurchase, testMenuitems.get(6), testMilk, testSize);
        purchaseLineitemRepo.save(purchaseLineitem2);


        assertEquals(12, purchaseLineitemRepo.findAll().size());
        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> fetchedBestSellers = menuitemRepo.findBestSellers(pageable);

        // ensures only top 3 returned
        assertEquals(3, fetchedBestSellers.size());


        Object[] row0 = fetchedBestSellers.get(0);
        assertEquals(testMenuitems.get(menuitemIds.get(0)).getId(), (Long) row0[0]);
        assertEquals(quantities.get(0), (Long) row0[2]);

        Object[] row1 = fetchedBestSellers.get(1);
        assertEquals(testMenuitems.get(menuitemIds.get(1)).getId(), (Long) row1[0]);
        assertEquals(quantities.get(1), (Long) row1[2]);

        Object[] row2 = fetchedBestSellers.get(2);
        assertEquals(testMenuitems.get(menuitemIds.get(2)).getId(), (Long) row2[0]);
        assertEquals(quantities.get(2), (Long) row2[2]);

        // ensures list in descending order
        assertTrue((Long) row0[2] > (Long) row1[2]);
        assertTrue((Long) row1[2] > (Long) row2[2]);









//        Pageable pageable = PageRequest.of(0, 3);
//        List<Object[]> bestSellerMenuitems(Pageable pageable);
    }
}


