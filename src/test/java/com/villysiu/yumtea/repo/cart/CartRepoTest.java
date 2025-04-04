package com.villysiu.yumtea.repo.cart;

import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.repo.tea.SizeRepo;
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
class CartRepoTest {
    @MockitoBean
    private SeedService seedService;
    @MockitoBean
    private StorageService storageService;

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private MenuitemRepo menuitemRepo;
    @Autowired
    private MilkRepo milkRepo;
    @Autowired
    private SizeRepo sizeRepo;


    private Account testAccount1;
    private Account testAccount2;
    private Category testCategory1;
    private Menuitem testMenuitem;
    private Cart testCart;
    private Milk testMilk;
    private Size testSize;
    private Sugar testSugar;
    private Temperature testTemperature;

    @BeforeEach
    void setUp() {
        Mockito.doNothing().when(seedService).init();
        Mockito.doNothing().when(storageService).init();

        testAccount1 = new Account("admin1@test.com", "testAdmin", "password2");
        accountRepo.save(testAccount1);

        testAccount2 = new Account("admin2@test.com", "testAdmin", "password2");
        accountRepo.save(testAccount2);

        testCategory1 = new Category("Beverages1");
        categoryRepo.save(testCategory1);

        testMilk = new Milk("testMilk");
        milkRepo.save(testMilk);

        testSize = new Size("testSize");
        sizeRepo.save(testSize);

        testSugar = Sugar.values()[0];
        testTemperature = Temperature.values()[0];

        testMenuitem = new Menuitem("Drink0", "IMG_0210.png", testCategory1, testMilk, testSugar, testTemperature, 6.0);
        menuitemRepo.save(testMenuitem);
        testCart = new Cart(testAccount1, testMenuitem, testMilk, testSize);
        cartRepo.save(testCart);
//        M String title, String imageUrl, Category category, Milk milk, Sugar sugar, Temperature temperature, double price

        for(int i=0; i<5; i++) {

            Menuitem menuitem = new Menuitem("Drink"+i, "IMG_0210.png", testCategory1, testMilk, testSugar, testTemperature, 6.0);
            menuitemRepo.save(menuitem);
            Cart cart = new Cart(testAccount2, menuitem, testMilk, testSize);
            cartRepo.save(cart);
        }

    }

    @Test
    void findByAccountIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature() {
        Cart fetchedCart = cartRepo.findByAccountIdAndMenuitemIdAndMilkIdAndSizeIdAndSugarAndTemperature(
                testAccount1.getId(), testMenuitem.getId(), testMilk.getId(), testSize.getId(), Sugar.ZERO, Temperature.HOT
        ).orElse(null);
        assertNotNull(fetchedCart);
    }

    @Test
    void findByAccountIdOrderByIdDesc() {
        List<Cart> cartListByAccount1 = cartRepo.findByAccountIdOrderByIdDesc(testAccount1.getId(), Cart.class);
        assertNotNull(cartListByAccount1);
        assertEquals(1, cartListByAccount1.size());

        List<Cart> cartListByAccount2 = cartRepo.findByAccountIdOrderByIdDesc(testAccount2.getId(), Cart.class);
        assertNotNull(cartListByAccount1);
        assertEquals(5, cartListByAccount2.size());

        assertTrue(cartListByAccount2.get(0).getId() > cartListByAccount2.get(4).getId());

    }

    @Test
    void findByIdAndAccountId() {
        Cart fetchedCart =
                cartRepo.findByIdAndAccountId(testCart.getId(), testAccount1.getId(), Cart.class).orElse(null);
        assertNotNull(fetchedCart);
        assertEquals(testAccount1, fetchedCart.getAccount());

        Cart fetchedCartNotBelongToAccount2 =
                cartRepo.findByIdAndAccountId(testCart.getId(), testAccount2.getId(), Cart.class).orElse(null);
        assertNull(fetchedCartNotBelongToAccount2);
    }

    @Test
    void findById() {
        Cart fetchedCart = cartRepo.findById(testCart.getId()).orElse(null);
        assertNotNull(fetchedCart);
        assertEquals(testMenuitem, fetchedCart.getMenuitem());
    }

    @Test
    void deleteAllByAccountId() {
        List<Cart> beforeList = cartRepo.findByAccountIdOrderByIdDesc(testAccount1.getId(), Cart.class);

        assertFalse(beforeList.isEmpty());

        cartRepo.deleteAllByAccountId(testAccount1.getId());

        List<Cart> afterList = cartRepo.findByAccountIdOrderByIdDesc(testAccount1.getId(), Cart.class);
        assertTrue(afterList.isEmpty());
    }
}