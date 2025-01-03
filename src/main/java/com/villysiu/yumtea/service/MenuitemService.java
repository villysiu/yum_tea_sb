package com.villysiu.yumtea.service;

import com.villysiu.yumtea.dto.tea.MenuitemDto;
import com.villysiu.yumtea.models.tea.Menuitem;

public interface MenuitemService {
    Menuitem createMenuitem(MenuitemDto menuitemDto) throws RuntimeException;
}
