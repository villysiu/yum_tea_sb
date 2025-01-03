package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.tea.MenuitemDto;
import com.villysiu.yumtea.models.tea.Category;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.service.MenuitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
import java.util.Optional;

@Service
public class MenuitemServiceImpl implements MenuitemService {

    @Autowired
    private MenuitemRepo menuitemRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private MilkRepo milkRepo;


    @Override
    public Menuitem createMenuitem(MenuitemDto menuitemDto) throws RuntimeException {
        System.out.println(menuitemDto.toString());

        Category category = categoryRepo.findById(menuitemDto.getCategoryId())
                .orElseThrow(()->new RuntimeException("Category not found."));
        Milk milk = milkRepo.findById(menuitemDto.getMilkId())
                .orElseThrow(()->new RuntimeException("Milk not found."));

        Menuitem menuitem = new Menuitem();

        menuitem.setTitle(menuitemDto.getTitle());
        menuitem.setDescription(menuitemDto.getDescription());
        menuitem.setImageUrl(menuitemDto.getImageUrl());
        menuitem.setPrice(menuitemDto.getPrice());

        menuitem.setCategory(category);
        menuitem.setMilk(milk);
        menuitem.setTemperature(menuitemDto.getTemperature());

        menuitemRepo.save(menuitem);

        return menuitem;
    }
}
