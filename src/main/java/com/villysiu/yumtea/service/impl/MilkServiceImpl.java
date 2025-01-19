package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.service.MilkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MilkServiceImpl implements MilkService {

    private final MilkRepo milkRepo;

    @Override
    public Milk updateMilk(Long id, Map<String, Object> milkDto) throws RuntimeException {
        Milk milk = milkRepo.findById(id).orElseThrow(()->new RuntimeException("Milk not found."));
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

    public Milk getMilkById(Long id) throws RuntimeException {
        return milkRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Milk not found."));
    }

    @Override
    public List<Milk> getMilks() {
        return milkRepo.findAll();
    }

    @Override
    public Milk createMilk(Milk milk) throws RuntimeException {
        return milkRepo.save(milk);
    }

    @Override
    public String deleteMilk(Long id) throws RuntimeException {
        milkRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Milk not found."));
        milkRepo.deleteById(id);
        return "Milk deleted";
    }
}
