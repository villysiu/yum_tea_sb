package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.repo.tea.SizeRepo;
import com.villysiu.yumtea.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepo sizeRepo;

    @Override
    public Size getSizeById(Long id) throws RuntimeException {
        return sizeRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Size not found."));
    }
}
