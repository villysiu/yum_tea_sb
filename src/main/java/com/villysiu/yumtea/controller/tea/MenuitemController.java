package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenuitemController {
    @Autowired
    private MenuitemRepo menuitemRepo;

    @GetMapping("/menuitems")
    public List<Menuitem> getMenuitems() {
        return menuitemRepo.findAll();
    }

    @PostMapping("/menuitem")
    public String addMenuitem(@RequestBody Menuitem menuitem) {
        menuitemRepo.save(menuitem);
        return menuitem.getTitle() + " added";
    }

    @PutMapping("/menuitem/{id}")
    public String updateMenuitem(@PathVariable Long id, @RequestBody Menuitem menuitem) {
        Menuitem updateMenuitem = menuitemRepo.findById(id).get();
        updateMenuitem.setTitle(menuitem.getTitle() );
        updateMenuitem.setDescription(menuitem.getDescription());
        updateMenuitem.setImageUrl(menuitem.getImageUrl());
        updateMenuitem.setPrice(menuitem.getPrice());
        updateMenuitem.setMilk(menuitem.getMilk());
        updateMenuitem.setCategory(menuitem.getCategory());
        menuitemRepo.save(updateMenuitem);
        return updateMenuitem.getTitle() + " updated";
    }

    @DeleteMapping("/menuitem/{id}")
    public String deleteMenuitem(@PathVariable Long id) {
        Menuitem deleteMenuitem = menuitemRepo.findById(id).get();
        menuitemRepo.delete(deleteMenuitem);
        return "Menuitem deleted";

    }
}
