package com.villysiu.yumtea.service;

import com.villysiu.yumtea.models.tea.Milk;

import java.util.Map;

public interface MilkService {
    Milk updateMilk(Long id, Map<String, Object> milkDto) throws RuntimeException;
    Milk getMilkById(Long id) throws RuntimeException;
}
