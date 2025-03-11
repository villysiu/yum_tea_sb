package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.service.MilkService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class MilkServiceImpl implements MilkService {

    @Autowired
    private final MilkRepo milkRepo;

    public MilkServiceImpl(MilkRepo milkRepo) {
        this.milkRepo = milkRepo;
    }

    @Override
    public Milk updateMilk(Long id, Map<String, Object> milkDto) {
        Milk milk = milkRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Milk not found."));

        for(Map.Entry<String, Object> milkDtoEntry : milkDto.entrySet()) {
            String field = milkDtoEntry.getKey();
            Object value = milkDtoEntry.getValue();

            switch (field) {
                case "title":
                    milk.setTitle((String)value);
                    break;

                case "price":
                    milk.setPrice((Double)value);
                    break;
                default:
                    break;
            }
            milkRepo.save(milk);
        }
        return milk;
    }

    public Milk getMilkById(Long id) {
        return milkRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Milk not found."));

    }

    @Override
    public List<Milk> getMilks() {
        return milkRepo.findAll();
    }

    @Override
    public Milk createMilk(Milk milk){
        System.out.println(milk.getTitle());
        System.out.println(milk.getPrice());
        Milk m = milkRepo.save(milk);
        System.out.println("savrd???");
        System.out.println(m.getTitle());
        return m;
    }

    @Override
    public void deleteMilk(Long id) {
        if (!milkRepo.existsById(id)) {
            throw new EntityNotFoundException("Milk not found");
        }
        milkRepo.deleteById(id);
    }
}
