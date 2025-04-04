package com.villysiu.yumtea.service.dataSeed;

import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.purchase.TaxRate;
import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.repo.purchase.TaxRepo;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.repo.tea.SizeRepo;
import com.villysiu.yumtea.repo.user.AccountRepo;
import com.villysiu.yumtea.service.storage.StorageException;
import com.villysiu.yumtea.service.tea.category.CategoryService;
import com.villysiu.yumtea.service.tea.milk.MilkService;
import com.villysiu.yumtea.service.user.AuthenticationService;
import com.villysiu.yumtea.service.user.RoleService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SeedServiceImpl implements SeedService{
    private final Logger logger = LoggerFactory.getLogger(SeedServiceImpl.class);
    //        private final Faker faker;

    private final AuthenticationService authenticationService;
    private final RoleService roleService;
    private final CategoryService categoryService;
    private final CategoryRepo categoryRepo;
    private final MilkRepo milkRepo;
    private final MilkService milkService;
    private final SizeRepo sizeRepo;
    private final MenuitemRepo menuitemRepo;
    private final PurchaseRepo purchaseRepo;
    private final PurchaseLineitemRepo purchaseLineitemRepo;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepo accountRepo;
    private final TaxRepo taxRepo;
    private final List<String> userList;
    private final Object[][] menuitemList;

    //    private final List<String> userList = List.of("springuser@gg.com", "springuser2@gg.com", "springuser3@gg.com");
    @Autowired
    public SeedServiceImpl(AuthenticationService authenticationService, RoleService roleService,
                           CategoryService categoryService, AccountRepo accountRepo, CategoryRepo categoryRepo,
                           MilkRepo milkRepo, MilkService milkService, SizeRepo sizeRepo, MenuitemRepo menuitemRepo,
                           PurchaseRepo purchaseRepo, PurchaseLineitemRepo purchaseLineitemRepo,
                           PasswordEncoder passwordEncoder, TaxRepo taxRepo, SeedProperties properties) {
        this.authenticationService = authenticationService;
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
        this.milkRepo = milkRepo;
        this.milkService = milkService;
        this.sizeRepo = sizeRepo;
        this.menuitemRepo = menuitemRepo;
        this.purchaseRepo = purchaseRepo;
        this.purchaseLineitemRepo = purchaseLineitemRepo;
        this.passwordEncoder = passwordEncoder;
        this.taxRepo = taxRepo;
        this.userList = properties.getUserList();
        this.menuitemList = properties.getMenuitems();
    }




    @Transactional
    @Override
    public void init() {

        try {
            createSuperAdmin();
            createUserAccounts();
            createCategories();
            createMilks();
            createSizes();
            createTaxes();
            createMenuitems();
            createPurchases();
        }catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error inserting database", e);
        }
        logger.info("database inserted successfully");
    }



    private void createSuperAdmin(){
        String superAdminEmail = "springadmin@gg.com";
        String superAdminPassword = "password1";
        String superAdminNickname = "Super Admin";

        if(!accountRepo.existsByEmail(superAdminEmail)){
            logger.info("Creating super admin account");

            SignupRequest signupRequest = new SignupRequest(superAdminNickname, superAdminEmail, "password1");

            Long adminId = authenticationService.signup(signupRequest);
            Account account = accountRepo.findById(adminId).get();
            logger.info("Assign Role_ADMIN");

            Role adminRole = roleService.getRoleByName("ROLE_ADMIN");

            account.setRoles(Collections.singleton(adminRole));
            logger.info("Saving super admin account");
            accountRepo.save(account);
            logger.info("saved super admin account");

        }
        logger.info("Super Admin account existed or created");
    }
    private void createUserAccounts(){

        for(String userEmail : userList){
            if(!accountRepo.existsByEmail(userEmail)){
                logger.info("Creating user account for {}", userEmail);

                SignupRequest signupRequest = new SignupRequest(userEmail.substring(0, userEmail.length()-7), userEmail, "password1");

                authenticationService.signup(signupRequest);
            }
        }
        logger.info("User account for springuser@gg.com, springuser2@gg.com, springuser3@gg.com existed or " +
                "created");
    }
    private void createCategories(){
        if(categoryRepo.count() == 0){

            List<String> categories = Arrays.asList("Black Tea", "Oolong Tea", "Jasmine Tea", "Caffeine Free");
            List<String> categoryImgs = Arrays.asList("blacktea.jpg", "oolong.jpg", "jasmine.jpg", "peppermint.jpg");
            for(int i =0; i<categories.size(); i++){
                logger.info("Creating {}", categories.get(i));
                Category category = new Category(categories.get(i), categoryImgs.get(i));
                logger.info("Saving {}", categories.get(i));
                categoryRepo.save(category);
                logger.info("Saved {}", categories.get(i));
            }
        }
        logger.info("Categories existed or created");
    }
    private void createMilks(){
        if(milkRepo.count() == 0){

            Object [][] milks = {
                    {"NA", 0.0}, {"No Milk",0.0}, {"Whole Milk", 0.0},  {"Nonfat Milk", 0.0}, {"Almond Milk",1.5}, {"Oat Milk", 1.5}, {"Coconut Milk", 1.5}
            };
            for(Object [] milk : milks){
                logger.info("Creating {}", milk[0]);
                Milk newMilk = new Milk((String) milk[0],(Double)milk[1]);
                logger.info("Saving  {}", milk[0]);
                milkRepo.save(newMilk);
                logger.info("Saved {}", milk[0]);
            }

        }
        logger.info("Milks existed or created");
    }
    private void createSizes(){
        if(sizeRepo.count() == 0){

            Object [][] sizes = {
                    {"8oz", 0.0}, {"12oz",2.0}, {"16oz", 4.0}
            };
            for(Object [] size : sizes){
                logger.info("Creating {}", size[0]);
                Size newSize = new Size((String) size[0],(Double)size[1]);
                logger.info("Saving  {}", size[0]);
                sizeRepo.save(newSize);
                logger.info("Saved {}", size[0]);
            }
        }
        logger.info("Size existed or created");
    }
    private void createMenuitems(){
        if(menuitemRepo.count() == 0){
            logger.info("Creating menuitems");

            for(Object [] menuitem : menuitemList){
                logger.info("Creating {}", menuitem[0]);
                Menuitem newMenuitem = new Menuitem(
                    (String)menuitem[0],
                    (String)menuitem[1],
                    categoryService.getCategoryByTitle((String) menuitem[2]),
                    milkService.getMilkByTitle((String) menuitem[3]),
                    (Sugar) menuitem[4],
                    (Temperature) menuitem[5],
                    (double)menuitem[6]
                );
                logger.info("Saving {}", menuitem[0]);
                menuitemRepo.save(newMenuitem);
                logger.info("Saved {}", menuitem[0]);
            }

        }
        logger.info("Menuitem existed or created");
    }
    private void createTaxes(){
        if(taxRepo.count() == 0){

            Object [][] taxes = {
                    {"WA", 10.0}, {"OR",0.0}, {"CA", 7.5}
            };
            for(Object [] tax : taxes){
                logger.info("Creating {}", tax[0]);
                TaxRate newTax = new TaxRate((String) tax[0],(Double)tax[1]);
                logger.info("Saving  {}", tax[0]);
                taxRepo.save(newTax);
                logger.info("Saved {}", tax[0]);
            }

        }
        logger.info("Tax existed or created");
    }

    private void createPurchases() {
        logger.info("Creating Purchase if less than 30");

        for(int j=(int)purchaseRepo.count()+1; j<=30; j++) {
            logger.info("Creating Purchase {} of 5", j);
            double total = 0.0;

            Purchase purchase = new Purchase(accountRepo.findByEmail(userList.get(getRandomInt(0, 3))).orElse(null));

            purchase.setPurchaseDate(getRandomDate());

            purchase.setPurchaseLineitemList(new ArrayList<>());

            logger.info("saving purchase");
            purchaseRepo.save(purchase);
            logger.info("saved purchase for {} on {}", purchase.getAccount().getEmail(), purchase.getPurchaseDate());

            Sugar[] sugars = Sugar.values();
            Temperature[] temperatures = Temperature.values();
            Integer randomPurchaseLineitemCount = getRandomInt(1, 4);

            for (int i = 0; i < randomPurchaseLineitemCount; i++) {
                logger.info("Creating PurchaseLineitem {} of {}", i + 1, randomPurchaseLineitemCount);
                PurchaseLineitem purchaseLineitem = new PurchaseLineitem();
                Double price = 0.0;
                purchaseLineitem.setPurchase(purchase);

                Menuitem menuitem = menuitemRepo.findById(getRandomLong(menuitemRepo.count())).get();
                purchaseLineitem.setMenuitem(menuitem);
                price += menuitem.getPrice();

                Milk milk = milkRepo.findById(getRandomLong(milkRepo.count())).get();
                purchaseLineitem.setMilk(milk);
                price += milk.getPrice();

                Size size = sizeRepo.findById(getRandomLong(sizeRepo.count())).get();
                purchaseLineitem.setSize(size);
                price += size.getPrice();

                purchaseLineitem.setSugar(sugars[getRandomInt(1, sugars.length)]);

                purchaseLineitem.setTemperature(temperatures[getRandomInt(1, temperatures.length)]);

                purchaseLineitem.setQuantity(getRandomInt(1, 6));

                purchaseLineitem.setPrice(price);

                logger.info("saving new purchaseLineitem:");
                logger.info("menuitem: {}, milk: {}, size: {}, sugar: {}, temp: {}, price: {}, quantity: {}",
                        purchaseLineitem.getMenuitem().getTitle(), purchaseLineitem.getMilk().getTitle(),
                        purchaseLineitem.getSize().getTitle(), purchaseLineitem.getSugar(),
                        purchaseLineitem.getTemperature(), purchaseLineitem.getPrice(), purchaseLineitem.getQuantity());
                purchaseLineitemRepo.save(purchaseLineitem);
                logger.info("successfully saved purchaseLineitem");
                purchase.getPurchaseLineitemList().add(purchaseLineitem);
                total += price * purchaseLineitem.getQuantity();

            }
            purchase.setTax(total * 0.1);
            purchase.setTip(total * 0.15);
            purchase.setTotal(total + purchase.getTax() + purchase.getTip());

            logger.info("saving purchase");
            purchaseRepo.save(purchase);
            logger.info("saved purchase");

        }
    }
    private Long getRandomLong(Long max) {
        return ThreadLocalRandom.current().nextLong(1, max+1);
    }

    private Integer getRandomInt(Integer min, Integer max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
    private Date getRandomDate() {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime randomDate = now.minusMonths(random.nextInt(12 + 1));
        randomDate = randomDate.minusDays(random.nextInt(30));
        return Date.from(randomDate.atZone(ZoneId.systemDefault()).toInstant());
    }
}
