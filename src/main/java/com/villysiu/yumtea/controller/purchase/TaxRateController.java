package com.villysiu.yumtea.controller.purchase;

import com.villysiu.yumtea.service.TaxRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxRateController {

    private final TaxRateService taxRateService;
    public TaxRateController(TaxRateService taxRateService) {
        this.taxRateService = taxRateService;
    }
    @GetMapping("/tax/{state}")
    public Double getTaxRate(@PathVariable("state") String state) {
        return taxRateService.getTaxRateByState(state);
    }
}
