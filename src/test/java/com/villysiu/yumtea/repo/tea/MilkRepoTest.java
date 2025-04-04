package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@DataJpaTest
//@ContextConfiguration(classes = TestConfig.class) // Use your test-specific config

class MilkRepoTest {
    @MockitoBean
    private SeedService seedService;
    @MockitoBean
    private StorageService storageService;
    @Autowired
    private MilkRepo milkRepo;
    @Test
    void findMilkByTitle() {
        Mockito.doNothing().when(seedService).init();
        Mockito.doNothing().when(storageService).init();
        Milk milk = new Milk();
        milk.setTitle("Whole Milk");
        milk.setPrice(3.0);
        milkRepo.save(milk);

        Milk fetchedMilk = milkRepo.findMilkByTitle("Whole Milk").orElse(null);
        assertNotNull(fetchedMilk);
        assertEquals("Whole Milk", fetchedMilk.getTitle());
        assertEquals(3.0, fetchedMilk.getPrice());
    }
}