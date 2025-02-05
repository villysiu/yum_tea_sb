package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.service.CategoryService;
import com.villysiu.yumtea.service.MenuitemService;
import com.villysiu.yumtea.service.MilkService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Service
public class MenuitemServiceImpl implements MenuitemService {
    private final CategoryService categoryService;
    private final MenuitemRepo menuitemRepo;

    private final MilkService milkService;

    MenuitemServiceImpl(MenuitemRepo menuitemRepo, CategoryService categoryService, MilkService milkService) {
        this.menuitemRepo = menuitemRepo;
        this.categoryService = categoryService;
        this.milkService = milkService;
    }

    @Override
    public List<Menuitem> getMenuitems(){
        return menuitemRepo.findAll();
    }

    @Override
    public Menuitem createMenuitem(MenuitemDto menuitemDto) {
        System.out.println(menuitemDto.toString());

        Category category = categoryService.getCategoryById(menuitemDto.getCategoryId());
        Milk milk = milkService.getMilkById(menuitemDto.getMilkId());

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
    public Menuitem updateMenuitem(Long id, Map<String, Object> menuitemDto) throws EntityNotFoundException {

        Menuitem menuitem = menuitemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menuitem not found."));

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
                    Long categoryId = (long) value;
                    Category category = categoryService.getCategoryById(categoryId);
                    menuitem.setCategory(category);
                    break;
                case "milkId":
                    Long milkId = (long) value;
                    Milk milk = milkService.getMilkById(milkId);
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
    public Menuitem getMenuitemById(Long id) throws EntityNotFoundException {
        return menuitemRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Menuitem not found."));
    }
    @Override
    public List<Menuitem> getMenuitemsByCategoryId(Long categoryId){
        return menuitemRepo.findByCategoryId(categoryId);
    }
    @Override
    public String deleteMenuitem(Long id) throws RuntimeException {
        if (!menuitemRepo.existsById(id)) {
            throw new EntityNotFoundException("Entity not found with id: " + id);
        }
        menuitemRepo.deleteById(id);
        return "Menuitem deleted.";
    }
}

