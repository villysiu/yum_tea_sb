package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.repo.tea.SizeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@SpringBootTest
class SizeServiceImplTest {

    @Mock
    private SizeRepo sizeRepo;

    @InjectMocks
    private SizeServiceImpl sizeService;


    private Size size;
    @BeforeEach
    void setUp() {
        size = new Size();
        size.setId(1L);
        size.setTitle("4oz");
        size.setPrice(5.9);

        // Arrange / mock
        when(sizeRepo.findById(1L)).thenReturn(Optional.of(size));

    }
    @Test
    void getSizeById() {
        //Act
        Size result = sizeService.getSizeById(1L);

        //Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(sizeRepo, times(1)).findById(1L);
    }
}