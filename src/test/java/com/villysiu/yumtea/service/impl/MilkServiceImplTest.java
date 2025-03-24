package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.service.tea.milk.MilkServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
//@ExtendWith(MockitoExtension.class) // for JUnit 5
class MilkServiceImplTest {

    @Mock
    private MilkRepo milkRepo;

    @InjectMocks
    private MilkServiceImpl milkService;


    private Milk milk;
    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
        milk = new Milk();
        milk.setTitle("testMilk");
        milk.setPrice(5.0);

    }

    @Test
    void createMilk() {
        //Arrange / Mock behavior
        when(milkRepo.save(any(Milk.class))).thenReturn(milk);

        //act
        Milk createdMilk = milkService.createMilk(milk);

        //aasert
        assertNotNull(createdMilk);
        verify(milkRepo, times(1)).save(milk);
    }
}