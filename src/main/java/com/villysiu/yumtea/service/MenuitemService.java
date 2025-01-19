package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.models.tea.Menuitem;

import java.util.List;
import java.util.Map;

public interface MenuitemService {
    List<Menuitem> getMenuitems();
    Menuitem createMenuitem(MenuitemDto menuitemDto) throws RuntimeException;
    Menuitem updateMenuitem(Long id, Map<String, Object> menuitemDto) throws RuntimeException;
    Menuitem getMenuitemById(Long id) throws RuntimeException;
    List<Menuitem> getMenuitemByCategoryId(Long categoryId);
    String deleteMenuitem(Long id) throws RuntimeException;
}
