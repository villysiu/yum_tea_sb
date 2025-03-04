package com.villysiu.yumtea.service;

import com.villysiu.yumtea.models.tea.Milk;

import java.util.List;
import java.util.Map;

public interface MilkService {

    Milk getMilkById(Long id);
    List<Milk> getMilks();

    Milk updateMilk(Long id, Map<String, Object> milkDto);

    Milk createMilk(Milk milk);
    void deleteMilk(Long id) ;
}
