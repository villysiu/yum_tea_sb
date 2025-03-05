package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.service.MilkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class MilkController {
    @Autowired
    private final MilkService milkService;

    MilkController(MilkService milkService) {
        this.milkService = milkService;
    }

    //read
    @GetMapping("/milks")
    public List<Milk> getMilks() {
        return milkService.getMilks();
    }

    @GetMapping("/milks/{id}")
    public Milk getMilkById(@PathVariable Long id) {
        return milkService.getMilkById(id);
    }

    // create
    @PostMapping(value="/milk")
    public ResponseEntity<Milk> createMilk(@RequestBody Milk milk) {

        return new ResponseEntity<>(milkService.createMilk(milk), HttpStatus.CREATED);
    }
//update
    @PatchMapping("/milk/{id}")
    public ResponseEntity<Milk> updateMilk(@PathVariable Long id, @RequestBody Map<String, Object> milkDto) {
        return ResponseEntity.ok(milkService.updateMilk(id, milkDto));
    }
//delete
    @DeleteMapping("/milk/{id}")
    public ResponseEntity<String> deleteMilk(@PathVariable Long id){
        milkService.deleteMilk(id);
        return new ResponseEntity<>("Milk deleted", HttpStatus.NO_CONTENT);
    }
}
