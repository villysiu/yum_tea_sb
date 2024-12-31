package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MilkController {
    @Autowired
    private MilkRepo milkRepo;

    @GetMapping("/milks")
    public List<Milk> getMilks() {
        return milkRepo.findAll();
    }

    @PostMapping(value="/milk")
    public String addMilk(@RequestBody Milk milk) {
        milkRepo.save(milk);
        return milk.getTitle() + " added";
    }

    @PutMapping("/milk/{id}")
    public String editMilk(@PathVariable Long id, @RequestBody Milk milk) {
        Milk updateMilk = milkRepo.findById(id).get();
        updateMilk.setTitle(milk.getTitle());
        updateMilk.setPrice(milk.getPrice());
        milkRepo.save(updateMilk);
        return updateMilk.getTitle() + " updated";
    }

    @DeleteMapping("/milk/{id}")
    public String deleteMilk(@PathVariable Long id) {
        Milk deleteMilk = milkRepo.findById(id).get();
        milkRepo.delete(deleteMilk);
        return "Milk deleted";
    }
}
