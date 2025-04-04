package com.villysiu.yumtea;

import com.villysiu.yumtea.service.dataSeed.SeedService;
import com.villysiu.yumtea.service.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class YumteaApplicationTests {

    @MockitoBean
    private SeedService seedService;
    @MockitoBean
    private StorageService storageService;
    @Test
    void contextLoads() {


        Mockito.doNothing().when(seedService).init();
        Mockito.doNothing().when(storageService).init();
    }

}
