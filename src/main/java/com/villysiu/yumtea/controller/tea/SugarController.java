package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.service.SizeService;
import com.villysiu.yumtea.service.SugarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SugarController {
    private final SugarService sugarService;
    public SugarController(SugarService sugarService) {
        this.sugarService = sugarService;
    }

    @GetMapping("/sugars")
    public List<Size> getSugar() {
        return sugarService.getSugar();
    }
}

