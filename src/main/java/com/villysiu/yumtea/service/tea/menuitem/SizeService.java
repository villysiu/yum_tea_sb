package com.villysiu.yumtea.service.tea.menuitem;

import com.villysiu.yumtea.models.tea.Size;

import java.util.List;
import java.util.Map;


public interface SizeService {
    Size getSizeById(Long id);
    List<Size> getSize();

    Size createSize(Size size);
    Size updateSize(Long id, Map<String, Object> sizeDto);
    void deleteSize(Long id);
}
