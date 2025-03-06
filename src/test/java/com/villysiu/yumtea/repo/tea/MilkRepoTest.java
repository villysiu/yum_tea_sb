package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Milk;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MilkRepoTest {
    @Autowired
    private MilkRepo milkRepo;
    @Test
    void findMilkByTitle() {
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