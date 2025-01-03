package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.service.MilkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MilkController {

    private final MilkRepo milkRepo;
    private final MilkService milkService;

    @GetMapping("/milks")
    public List<Milk> getMilks() {
        return milkRepo.findAll();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/milk")
    public ResponseEntity<Milk> createMilk(@RequestBody Milk milk) {
        milkRepo.save(milk);
        return new ResponseEntity<>(milk, HttpStatus.CREATED);
    }

    @PatchMapping("/milk/{id}")
    public ResponseEntity<Milk> updateMilk(@PathVariable Long id, @RequestBody Map<String, Object> milkDto) {
        Milk milk = milkService.updateMilk(id, milkDto);
        return ResponseEntity.ok(milk);
    }

    @DeleteMapping("/milk/{id}")
    public ResponseEntity<String> deleteMilk(@PathVariable Long id) {
        Milk milk = milkRepo.findById(id).orElseThrow(()-> new RuntimeException("milk not found"));
        milkRepo.delete(milk);
        return ResponseEntity.ok("milk deleted.");
    }
}
