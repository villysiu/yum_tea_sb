package com.villysiu.yumtea.repo.purchase;

import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import com.villysiu.yumtea.models.purchase.TaxRate;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaxRepoTest {
    @MockitoBean
    private SeedService seedService;
    @MockitoBean
    private StorageService storageService;
    @Autowired
    private TaxRepo taxRepo;
    @Test
    void findByState() {
        Mockito.doNothing().when(seedService).init();
        Mockito.doNothing().when(storageService).init();
        TaxRate testTaxRate = new TaxRate("WA", 10.0);
        taxRepo.save(testTaxRate);

        TaxRate fetchedTaRate = taxRepo.findByState("WA").orElse(null);
        assertNotNull(fetchedTaRate);
        assertEquals("WA", fetchedTaRate.getState());
        assertEquals(10.0, fetchedTaRate.getRate());

    }
}