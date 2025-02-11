package com.villysiu.yumtea.controller.tea;

import com.villysiu.yumtea.models.tea.Sugar;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SugarController {

    @GetMapping("/sugars")
    public List<Sugar> getSugar(){
        return Arrays.asList(Sugar.values());
    }
}

