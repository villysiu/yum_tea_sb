package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Sugar;
import com.villysiu.yumtea.models.tea.Temperature;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TemperatureController {
    @GetMapping("/temperatures")
    public List<Temperature> getTemps(){
        return Arrays.asList(Temperature.values());
    }
}
