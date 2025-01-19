package com.villysiu.yumtea.service;

import com.villysiu.yumtea.models.tea.Milk;

import java.util.List;
import java.util.Map;

public interface MilkService {
    Milk updateMilk(Long id, Map<String, Object> milkDto) throws RuntimeException;
    Milk getMilkById(Long id) throws RuntimeException;
    List<Milk> getMilks();
    Milk createMilk(Milk milk) throws RuntimeException;
    String deleteMilk(Long id) throws RuntimeException;
}
