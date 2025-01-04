package com.villysiu.yumtea.service;

import com.villysiu.yumtea.models.tea.Size;


public interface SizeService {
    Size getSizeById(Long id) throws RuntimeException;
}
