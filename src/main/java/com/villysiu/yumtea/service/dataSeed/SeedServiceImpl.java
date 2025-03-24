package com.villysiu.yumtea.service.dataSeed;

import com.villysiu.yumtea.dto.request.SigninRequest;
import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.models.purchase.Purchase;
import com.villysiu.yumtea.models.purchase.PurchaseLineitem;
import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.models.user.Role;
import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import com.villysiu.yumtea.repo.purchase.PurchaseRepo;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.repo.tea.SizeRepo;
import com.villysiu.yumtea.repo.user.AccountRepo;
import com.villysiu.yumtea.service.tea.category.CategoryService;
import com.villysiu.yumtea.service.tea.milk.MilkService;
import com.villysiu.yumtea.service.user.AuthenticationService;
import com.villysiu.yumtea.service.user.RoleService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SeedServiceImpl implements SeedService{
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
    private final Logger logger = LoggerFactory.getLogger(SeedServiceImpl.class);

//        private final Faker faker;
    private final AccountRepo accountRepo;
    @Autowired
    public SeedServiceImpl(AuthenticationService authenticationService, RoleService roleService, CategoryService categoryService, AccountRepo accountRepo, CategoryRepo categoryRepo, MilkRepo milkRepo, MilkService milkService, SizeRepo sizeRepo, MenuitemRepo menuitemRepo, PurchaseRepo purchaseRepo, PurchaseLineitemRepo purchaseLineitemRepo, PasswordEncoder passwordEncoder) {
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
    }




        @Transactional
        @Override
        public void init() {
            // Generate and insert fake data into the database
            // create super user  with ROLE_ADMIN if not already exist
            // email: springadmin@gg.com
            String superAdminEmail = "springadmin@gg.com";
            String superAdminPassword = "password1";
            String superAdminNickname = "Super Admin";
            System.out.println(accountRepo.existsByEmail(superAdminEmail));

            if(!accountRepo.existsByEmail(superAdminEmail)){
                logger.info("Creating super admin account");

                Account account = new Account();
                account.setEmail(superAdminEmail);
                account.setNickname(superAdminNickname);
                account.setPassword(passwordEncoder.encode(superAdminPassword));
                logger.info("Assign Role_ADMIN");


                Role adminRole = roleService.getRoleByName("ROLE_ADMIN");

                account.setRoles(Collections.singleton(adminRole));
                accountRepo.save(account);
                System.out.println(accountRepo.existsByEmail(superAdminEmail));

            }
            logger.info("Super Admin account existed or created");

            List<String> userList = List.of("springuser@gg.com", "springuser2@gg.com", "springuser3@gg.com");
            for(String userEmail : userList){
                if(!accountRepo.existsByEmail(userEmail)){
                    logger.info("Creating user account for {}", userEmail);

//                    Account account = new Account(userEmail,  , "password1" );
                    SignupRequest signupRequest = new SignupRequest();
                    signupRequest.setEmail(userEmail);
                    signupRequest.setPassword("password1");
                    signupRequest.setNickname(userEmail.substring(0, userEmail.length()-7));

                    logger.info("Saving {}",userEmail);
                    authenticationService.signup(signupRequest);
                    logger.info("Saved {}",userEmail);



                }
            }
            logger.info("User account for springuser@gg.com, springuser2@gg.com, springuser3@gg.com existed or " +
                    "created");

            System.out.println(categoryRepo.count() );
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

            if(milkRepo.count() == 0){

                List<String> milkNames = Arrays.asList("NA", "No Milk", "Whole Milk", "Nonfat Milk", "Almond Milk", "Oat Milk", "Coconut Milk");
                List<Double> milkPrices = Arrays.asList(0.0, 0.0, 0.0, 0.0, 1.5, 1.5, 1.5);
                for(int i=0; i<milkNames.size(); i++){
                    logger.info("Creating {}", milkNames.get(i));
                    Milk milk = new Milk(milkNames.get(i), milkPrices.get(i));
                    logger.info("Saving {}", milkNames.get(i));
                    milkRepo.save(milk);
                    logger.info("Saved {}", milkNames.get(i));
                }


            }
            logger.info("Milks existed or created");

            if(sizeRepo.count() == 0){

                List<String> sizeNames = Arrays.asList("8oz", "12oz", "16oz");
                List<Double> sizePrices = Arrays.asList(0.0, 2.0, 4.0);
                for(int i=0; i<sizeNames.size(); i++){
                    logger.info("Creating {}", sizeNames.get(i));
                    Size size = new Size(sizeNames.get(i), sizePrices.get(i));
                    logger.info("Saving  {}", sizeNames.get(i));
                    sizeRepo.save(size);
                    logger.info("Saved {}", sizeNames.get(i));
                }
            }
            logger.info("Size existed or created");


            if(menuitemRepo.count() == 0){
                logger.info("Creating menuitems");

                // default Sugar.ZERO and  Temperature.FREE
//                String title, String imageUrl, Category category, Milk milk, Sugar sugar, Temperature temperature, double price
                    Object[][] menuitems = {
                            {"Chai", "chai.jpg","Black Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Earl Grey", "earl-grey-sp.jpg", "Black Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"English Breakfast","English-Breakfast.jpg", "Black Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},

                            {"Jasmine", "jasmine-green.webp", "Jasmine Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Dragon Pearl", "Dragon-Pearl-Jasmine.jpg", "Jasmine Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Silver Needle", "Jasmine-Silver-Needles.webp", "Jasmine Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Genmaicha", "Genmaicha.jpg", "Jasmine Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Hojicha", "Hojicha.webp", "Jasmine Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},

                            {"Iced Strawberry Lemonade", "Iced-Strawberry-Lemonade.jpg", "Caffeine Free", "NA", Sugar.NA, Temperature.ICED, 5.00},
                            {"Tumeric Ginger", "Turmeric-Ginger.webp", "Caffeine Free", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Chamomile", "Chamomile-Tea.webp", "Caffeine Free", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Hibiscus Berry", "hibiscus_berry.jpg", "Caffeine Free", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Mint", "Mint-Tea.jpg", "Caffeine Free", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Peppermint", "peppermint-tea.jpg", "Caffeine Free", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Frozen Lemonade", "lemonade.webp", "Caffeine Free", "NA",  Sugar.NA, Temperature.ICED, 5.00},
                            {"Hot Chocolate", "hot-chocolate.jpg", "Caffeine Free", "Whole Milk", Sugar.NA, Temperature.HOT, 5.00},

                            {"Oolong", "oolong.webp", "Oolong Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"Iron Goddess of Mercy", "Iron-Goddess.webp", "Oolong Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
                            {"High Mountain Tea", "High-Mountain.webp", "Oolong Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},

                    };

                    for(Object [] menuitem : menuitems){
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

                    purchaseLineitem.setSugar(sugars[getRandomInt(0, sugars.length)]);

                    purchaseLineitem.setTemperature(temperatures[getRandomInt(0, temperatures.length)]);

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
            System.out.println("Fake data inserted successfully!");

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
