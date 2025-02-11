package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.service.SizeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SizeController {
    private final SizeService sizeService;
    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping("/sizes")
    public List<Size> getSize() {
        return sizeService.getSize();
    }
}
