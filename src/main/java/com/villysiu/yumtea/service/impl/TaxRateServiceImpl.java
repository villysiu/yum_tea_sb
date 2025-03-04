package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.purchase.TaxRate;
import com.villysiu.yumtea.repo.purchase.TaxRepo;
import com.villysiu.yumtea.service.TaxRateService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxRateServiceImpl implements TaxRateService {
    @Autowired
    private final TaxRepo taxRepo;

    public TaxRateServiceImpl(TaxRepo taxRepo) {
        this.taxRepo = taxRepo;
    }

    @Override
    public Double getTaxRateByState(String state) {
        TaxRate taxRate = taxRepo.findByState(state).orElse(null);
        return taxRate == null ? 0.0 : taxRate.getRate();
    }
}
