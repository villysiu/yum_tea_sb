package com.villysiu.yumtea.repo.tea;

import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class SizeRepoTest {
    @MockitoBean
    private SeedService seedService;
    @MockitoBean
    private StorageService storageService;
    @Autowired
    private SizeRepo sizeRepo;

    @Test
    void findByTitle() {
        Mockito.doNothing().when(seedService).init();
        Mockito.doNothing().when(storageService).init();
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