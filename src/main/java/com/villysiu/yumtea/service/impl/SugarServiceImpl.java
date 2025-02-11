package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.repo.tea.SugarRepo;
import com.villysiu.yumtea.service.SugarService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SugarServiceImpl implements SugarService {
    private final SugarRepo sugarRepo;

    public SugarServiceImpl(SugarRepo sugarRepo) {
        this.sugarRepo = sugarRepo;
    }

    @Override
    public List<Size> getSugar() {
        return sugarRepo.findAll();
    }
}
