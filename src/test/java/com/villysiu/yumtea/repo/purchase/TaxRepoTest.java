package com.villysiu.yumtea.repo.purchase;

import org.junit.jupiter.api.Test;
import com.villysiu.yumtea.models.purchase.TaxRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaxRepoTest {

    @Autowired
    private TaxRepo taxRepo;
    @Test
    void findByState() {
        TaxRate testTaxRate = new TaxRate("WA", 10.0);
        taxRepo.save(testTaxRate);

        TaxRate fetchedTaRate = taxRepo.findByState("WA").orElse(null);
        assertNotNull(fetchedTaRate);
        assertEquals("WA", fetchedTaRate.getState());
        assertEquals(10.0, fetchedTaRate.getRate());

    }
}