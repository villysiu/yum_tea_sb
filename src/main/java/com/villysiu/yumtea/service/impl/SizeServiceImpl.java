package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.repo.tea.SizeRepo;
import com.villysiu.yumtea.service.SizeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    @Autowired
    private final SizeRepo sizeRepo;

    @Override
    public Size getSizeById(Long id) {
        return sizeRepo.findById(id)
                .orElse(sizeRepo.findByTitle("8oz")
                        .orElse(
                                sizeRepo.save(new Size("8oz"))
                        ));
    }

    @Override
    public List<Size> getSize() {
        return sizeRepo.findAll();
    }

    @Override
    public Size createSize(Size size) {
        return sizeRepo.save(size);
    }

    @Override
    public Size updateSize(Long id, Map<String, Object> sizeDto){
        Size size = sizeRepo.findById(id).orElseThrow(()->new NoSuchElementException("Size not found."));
        for (Map.Entry<String, Object> entry : sizeDto.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("title")) {
                size.setTitle((String) value);
            }
            else if(key.equals("price")) {
                size.setPrice((Double) value);
            }
        }
        return sizeRepo.save(size);
    }

    @Override
    public void deleteSize(Long id) {
        if(sizeRepo.existsById(id)) {
            sizeRepo.deleteById(id);
        }
        throw new EntityNotFoundException("Size not found.");
    }
}
