package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.service.tea.menuitem.MenuitemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MenuitemController {

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

    // ADMIN ONLY
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
        menuitemService.deleteMenuitem(id);
        return new ResponseEntity<>("Menuitem deleted", HttpStatus.NO_CONTENT);
    }

  

    @PatchMapping("/menuitem/{id}/toggleActive")
    public ResponseEntity<?> toggleActiveMenuitem(@PathVariable Long id) {
            menuitemService.toggleActiveMenuitem(id);
            return new ResponseEntity<>(HttpStatus.OK);

    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " +e.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
