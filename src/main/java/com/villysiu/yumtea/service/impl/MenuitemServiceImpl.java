package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.service.MenuitemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class MenuitemServiceImpl implements MenuitemService {


    private final MenuitemRepo menuitemRepo;

    private final CategoryRepo categoryRepo;

    private final MilkRepo milkRepo;


    @Override
    public Menuitem createMenuitem(MenuitemDto menuitemDto) throws RuntimeException {
        System.out.println(menuitemDto.toString());

        Category category = categoryRepo.findById(menuitemDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found."));
        Milk milk = milkRepo.findById(menuitemDto.getMilkId())
                .orElseThrow(() -> new RuntimeException("Milk not found."));

        Menuitem menuitem = new Menuitem();

        menuitem.setTitle(menuitemDto.getTitle());
        menuitem.setDescription(menuitemDto.getDescription());
        menuitem.setImageUrl(menuitemDto.getImageUrl());
        menuitem.setPrice(menuitemDto.getPrice());

        menuitem.setCategory(category);
        menuitem.setMilk(milk);
        menuitem.setTemperature(menuitemDto.getTemperature());
        menuitem.setSugar(menuitemDto.getSugar());
        menuitemRepo.save(menuitem);

        return menuitem;
    }

    @Override
    public Menuitem updateMenuitem(Long id, Map<String, Object> menuitemDto) throws RuntimeException {

        Menuitem menuitem = menuitemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Menuitem not found."));

        for (Map.Entry<String, Object> entry : menuitemDto.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            System.out.println(key + " = " + value);
            switch (key) {
                case "title":
                    menuitem.setTitle((String) value);
                    break;
                case "description":
                    menuitem.setDescription((String) value);
                    break;
                case "imageUrl":
                    menuitem.setImageUrl((String) value);
                    break;
                case "price":
                    menuitem.setPrice((Double) value);
                    break;
                case "categoryId":
                    Long longValue = Long.parseLong(String.valueOf(value));
                    Category category = categoryRepo.findById(longValue)
                            .orElseThrow(() -> new RuntimeException("Category not found."));
                    menuitem.setCategory(category);
                    break;
                case "milkId":
                    Long longmValue = Long.parseLong(String.valueOf(value));
                    Milk milk = milkRepo.findById(longmValue)
                            .orElseThrow(() -> new RuntimeException("Milk not found."));
                    menuitem.setMilk(milk);
                    break;
                case "temperature":
                    menuitem.setTemperature((Temperature) value);
                    break;
                case "sugar":
                    menuitem.setSugar((Sugar) value);
                    break;
                default:
                    break;
            }
        }
        menuitemRepo.save(menuitem);
        return menuitem;
    }

    @Override
    public Menuitem getMenuitemById(Long id) throws RuntimeException {

        return menuitemRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Menuitem not found."));
//        .orElse(null);
    }
}

