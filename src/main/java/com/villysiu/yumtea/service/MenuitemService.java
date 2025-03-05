package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.dto.response.BestSellerDto;
import com.villysiu.yumtea.models.tea.Menuitem;

import java.util.List;
import java.util.Map;

public interface MenuitemService {
    List<Menuitem> getMenuitems();
    List<Menuitem> getMenuitemsByCategoryId(Long categoryId);
    List<BestSellerDto> getBestsellers();

    Menuitem getMenuitemById(Long id);

    Menuitem createMenuitem(MenuitemDto menuitemDto);
    Menuitem updateMenuitem(Long id, Map<String, Object> menuitemDto);
    String deleteMenuitem(Long id);
}
