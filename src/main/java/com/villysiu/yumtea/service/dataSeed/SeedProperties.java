package com.villysiu.yumtea.service.dataSeed;

import com.villysiu.yumtea.dto.request.SignupRequest;
import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties("seed")
public class SeedProperties {
    /**
     * data for super admin
     */
    private final List<String> userList = List.of("springuser@gg.com", "springuser2@gg.com", "springuser3@gg.com");

    // default Sugar.ZERO and  Temperature.FREE
//                String title, String imageUrl, Category category, Milk milk, Sugar sugar, Temperature temperature, double price

    private final Object[][] menuitems = {
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
            {"Hot Chocolate", "hot-chocolate.jpg", "Caffeine Free", "NA", Sugar.NA, Temperature.HOT, 5.00},

            {"Oolong", "oolong.webp", "Oolong Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
            {"Iron Goddess of Mercy", "Iron-Goddess.webp", "Oolong Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},
            {"High Mountain Tea", "High-Mountain.webp", "Oolong Tea", "No Milk", Sugar.ZERO, Temperature.FREE, 5.00},

    };
}
