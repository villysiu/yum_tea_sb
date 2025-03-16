package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.dto.response.BestSellerDto;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.service.MenuitemService;
import com.villysiu.yumtea.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/menuitem/img/{id}")
    public ResponseEntity<?> updateMenuitemImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        System.out.println("in upload contriller");

        try {
            storageService.store(file);
            Map<String, Object> menuitemDto = new HashMap<>();
            menuitemDto.put("imageUrl", file.getOriginalFilename());
            Menuitem menuitem = menuitemService.updateMenuitem(id, menuitemDto);
            return new ResponseEntity<>(menuitem, HttpStatus.OK);
//            return new ResponseEntity<>("success upload image", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/menuitem/img/{id}")
    public ResponseEntity<?> deleteMenuitemImage(@PathVariable Long id) {
        System.out.println("in delete img controller");

        try {
//            research hoe to delete backend or data
//            storageService.store(file);
            Map<String, Object> menuitemDto = new HashMap<>();
            menuitemDto.put("imageUrl", "");
            Menuitem menuitem = menuitemService.updateMenuitem(id, menuitemDto);
            return new ResponseEntity<>(menuitem, HttpStatus.OK);
//            return new ResponseEntity<>("success upload image", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
