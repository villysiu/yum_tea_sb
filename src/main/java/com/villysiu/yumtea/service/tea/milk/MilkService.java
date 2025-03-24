package com.villysiu.yumtea.service.tea.milk;

import com.villysiu.yumtea.models.tea.Milk;

import java.util.List;
import java.util.Map;

public interface MilkService {

    Milk getMilkById(Long id);
    Milk getMilkByTitle(String title);
    List<Milk> getMilks();

    Milk updateMilk(Long id, Map<String, Object> milkDto);

    Milk createMilk(Milk milk);
    void deleteMilk(Long id) ;
}
