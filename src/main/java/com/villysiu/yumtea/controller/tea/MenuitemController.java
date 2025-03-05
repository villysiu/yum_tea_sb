package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.dto.response.BestSellerDto;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.service.MenuitemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MenuitemController {
    @Autowired
    private final MenuitemService menuitemService;
    MenuitemController(MenuitemService menuitemService) {
        this.menuitemService = menuitemService;
    }
//read
    @GetMapping("/menuitems")
    public List<Menuitem> getMenuitems() {
        return menuitemService.getMenuitems();

    }

    @GetMapping("/category/{id}/menuitems")
    public List<Menuitem> getMenuitemsByCategory(@PathVariable Long id) {
        return menuitemService.getMenuitemsByCategoryId(id);
    }
    @GetMapping("/bestsellers")
    public List<BestSellerDto> getBestsellers() {
        return menuitemService.getBestsellers();
    }

    //Create
    @PostMapping("/menuitem")
    public ResponseEntity<Menuitem> createMenuitem(@RequestBody MenuitemDto menuitemDto) {
        Menuitem menuitem = menuitemService.createMenuitem(menuitemDto);
        return new ResponseEntity<>(menuitem, HttpStatus.CREATED);
    }
//Update
    @PatchMapping("/menuitem/{id}")
    public ResponseEntity<Menuitem> updateMenuitem(@PathVariable Long id, @RequestBody Map<String, Object> menuitemDto) {
        Menuitem menuitem = menuitemService.updateMenuitem(id, menuitemDto);
        return new ResponseEntity<>(menuitem, HttpStatus.OK);
    }
//delete
    @DeleteMapping("/menuitem/{id}")
    public ResponseEntity<String> deleteMenuitem(@PathVariable Long id) {
        return new ResponseEntity<>(menuitemService.deleteMenuitem(id), HttpStatus.NO_CONTENT);
    }

}
