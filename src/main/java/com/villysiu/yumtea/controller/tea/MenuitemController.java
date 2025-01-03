package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.dto.tea.MenuitemDto;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.service.MenuitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MenuitemController {
    @Autowired
    private MenuitemRepo menuitemRepo;

    @Autowired
    private MenuitemService menuitemService;
    @GetMapping("/menuitems")
    public List<Menuitem> getMenuitems() {
        return menuitemRepo.findAll();
    }

    @PostMapping("/menuitem")
    public ResponseEntity<Menuitem> createMenuitem(@RequestBody MenuitemDto menuitemDto) {
        Menuitem menuitem = menuitemService.createMenuitem(menuitemDto);
        return new ResponseEntity<>(menuitem, HttpStatus.CREATED);
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
    @PatchMapping("/menuitem/{id}")
    public ResponseEntity<Menuitem> updateMenuitem(@PathVariable Long id, @RequestBody Map<String, Object> menuitemDto) {
        Menuitem menuitem = menuitemService.updateMenuitem(id, menuitemDto);
        return new ResponseEntity<>(menuitem, HttpStatus.CREATED);
    }
    @DeleteMapping("/menuitem/{id}")
    public String deleteMenuitem(@PathVariable Long id) {
        Menuitem deleteMenuitem = menuitemRepo.findById(id).get();
        menuitemRepo.delete(deleteMenuitem);
        return "Menuitem deleted";

    }
}
