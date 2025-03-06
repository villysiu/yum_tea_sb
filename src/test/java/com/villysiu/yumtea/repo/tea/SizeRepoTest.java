package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Size;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class SizeRepoTest {
    @Autowired
    private SizeRepo sizeRepo;

    @Test
    void findByTitle() {
        Size size = new Size();
        size.setTitle("5oz");
        size.setPrice(3.0);
        sizeRepo.save(size);

        Size fetchedSize = sizeRepo.findByTitle("5oz").orElse(null);
        assertNotNull(fetchedSize);
        assertEquals("5oz", fetchedSize.getTitle());
        assertEquals(3.0, fetchedSize.getPrice());
    }

}