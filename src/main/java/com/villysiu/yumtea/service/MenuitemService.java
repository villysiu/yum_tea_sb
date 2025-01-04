package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.input.MenuitemDto;
import com.villysiu.yumtea.models.tea.Menuitem;

import java.util.Map;

public interface MenuitemService {
    Menuitem createMenuitem(MenuitemDto menuitemDto) throws RuntimeException;
    Menuitem updateMenuitem(Long id, Map<String, Object> menuitemDto) throws RuntimeException;
    Menuitem getMenuitemById(Long id) throws RuntimeException;
}
