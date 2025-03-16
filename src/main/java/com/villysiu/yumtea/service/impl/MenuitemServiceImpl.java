package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.dto.response.BestSellerDto;
import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.repo.tea.CategoryRepo;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.service.CategoryService;
import com.villysiu.yumtea.service.MenuitemService;
import com.villysiu.yumtea.service.MilkService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Service
public class MenuitemServiceImpl implements MenuitemService {
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final MenuitemRepo menuitemRepo;
    @Autowired
    private final MilkService milkService;

    MenuitemServiceImpl(MenuitemRepo menuitemRepo, CategoryService categoryService, MilkService milkService) {
        this.menuitemRepo = menuitemRepo;
        this.categoryService = categoryService;
        this.milkService = milkService;
    }

//    Create
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

//    Update
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
                    System.out.println(value instanceof String);
                    Double updatePrice = Double.parseDouble(value.toString());


                    menuitem.setPrice(updatePrice);

                    break;
                case "categoryId":
//                    System.out.println(value instanceof String);
//                    System.out.println(value instanceof Integer);
                    Integer catInt = (Integer) value;
                    Long categoryId = catInt.longValue();
                    Category category = categoryService.getCategoryById(categoryId);
                    menuitem.setCategory(category);
                    break;
                case "milkId":
//                    System.out.println(value instanceof String);
//                    System.out.println(value instanceof Integer);
//                    Long milkId = Long.parseLong((String) value);

                    Integer mkInt = (Integer) value;
                    Long milkId = mkInt.longValue();

                    Milk milk = milkService.getMilkById(milkId);

                    menuitem.setMilk(milk);
                    break;
                case "temperature":
//                    System.out.println(value instanceof Temperature);
//                    System.out.println(value instanceof String);
                    Temperature temperature = Temperature.valueOf((String) value);
                    menuitem.setTemperature(temperature);
                    break;
                case "sugar":
                    Sugar sugar = Sugar.valueOf((String) value);
                    menuitem.setSugar(sugar);
//                    menuitem.setSugar((Sugar) value);
                    break;
                default:
                    break;
            }
        }
        menuitemRepo.save(menuitem);
        return menuitem;
    }

//    Read
    @Override
    public List<Menuitem> getMenuitems(){
        return menuitemRepo.findAll();
    }
    @Override
    public Menuitem getMenuitemById(Long id) throws EntityNotFoundException {
        return menuitemRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Menuitem not found."));
    }
    @Override
    public List<Menuitem> getMenuitemsByCategoryId(Long categoryId){
        return menuitemRepo.findByCategoryIdQuery(categoryId);
//        return menuitemRepo.findByCategoryId(categoryId);
    }
    @Override
    public List<BestSellerDto> getBestsellers(){
        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> bestsellers = menuitemRepo.findBestSellers(pageable);
        List<BestSellerDto> bestSellerDtos = new ArrayList<>();
        for (Object[] row : bestsellers) {
            BestSellerDto dto = new BestSellerDto();
            dto.setMenuitemId((Long) row[0]);
            dto.setMenuitemTitle((String) row[1]);
            dto.setCount((Long) row[2]);
            bestSellerDtos.add(dto);

        }

        return bestSellerDtos;
    }

    //    Delete
    @Override
    public String deleteMenuitem(Long id) throws RuntimeException {
        if (!menuitemRepo.existsById(id)) {
            throw new EntityNotFoundException("Entity not found with id: " + id);
        }
        menuitemRepo.deleteById(id);
        return "Menuitem deleted.";
    }
}

