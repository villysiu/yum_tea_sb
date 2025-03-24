package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.service.tea.menuitem.SizeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SizeController {
    private final SizeService sizeService;
    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }
// read
    @GetMapping("/sizes")
    public List<Size> getSzie() {
        return sizeService.getSize();
    }

    // create
    @PostMapping(value="/size")
    public ResponseEntity<Size> createSize(@RequestBody Size size) {

        return new ResponseEntity<>(sizeService.createSize(size), HttpStatus.CREATED);
    }
    //update
    @PatchMapping("/size/{id}")
    public ResponseEntity<Size> updateSize(@PathVariable Long id, @RequestBody Map<String, Object> sizeDto) {
        return ResponseEntity.ok(sizeService.updateSize(id, sizeDto));
    }
    //delete
    @DeleteMapping("/size/{id}")
    public ResponseEntity<String> deleteSize(@PathVariable Long id){
        sizeService.deleteSize(id);
        return new ResponseEntity<>("Size deleted", HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
