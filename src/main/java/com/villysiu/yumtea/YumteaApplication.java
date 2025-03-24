package com.villysiu.yumtea;

import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageProperties;
import com.villysiu.yumtea.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class})

public class YumteaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YumteaApplication.class, args);
    }
    @Autowired
    private SeedService seedService;

    @Bean
    CommandLineRunner init(StorageService storageService) {
        System.out.println("I am preparing the photo storage?");
        return (args) -> {
//            storageService.deleteAll();
            storageService.init();
        };
    }
    @Bean
  CommandLineRunner initSeed(SeedService seedService) {
        System.out.println("I am preparing the database?");
        return args -> {
            seedService.init();





        };

    }

}
