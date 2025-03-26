package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.service.tea.menuitem.MenuitemService;
import com.villysiu.yumtea.service.storage.StorageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuitemController {

    private final MenuitemService menuitemService;
    private final StorageService storageService;

    @Autowired
    MenuitemController(MenuitemService menuitemService, StorageService storageService) {
        this.menuitemService = menuitemService;
        this.storageService = storageService;
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

    @PostMapping("/menuitem/{id}/img")
    public ResponseEntity<?> updateMenuitemImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

            storageService.store(file);
            Map<String, Object> menuitemDto = new HashMap<>();
            menuitemDto.put("imageUrl", file.getOriginalFilename());
            Menuitem menuitem = menuitemService.updateMenuitem(id, menuitemDto);
            return new ResponseEntity<>(menuitem, HttpStatus.OK);

    }
    @DeleteMapping("/menuitem/img/{id}")
    public ResponseEntity<?> deleteMenuitemImage(@PathVariable Long id) {
//            research hoe to delete backend or data
//            storageService.store(file);
            Map<String, Object> menuitemDto = new HashMap<>();
            menuitemDto.put("imageUrl", "");
            Menuitem menuitem = menuitemService.updateMenuitem(id, menuitemDto);
            return new ResponseEntity<>(menuitem, HttpStatus.OK);


    }

    @PatchMapping("/menuitem/toggleActive/{id}")
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
