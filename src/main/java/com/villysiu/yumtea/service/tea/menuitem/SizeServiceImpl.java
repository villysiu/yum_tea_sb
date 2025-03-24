package com.villysiu.yumtea.service.tea.menuitem;

import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.repo.tea.SizeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class SizeServiceImpl implements SizeService {
    private final SizeRepo sizeRepo;

    @Autowired
    public SizeServiceImpl(SizeRepo sizeRepo) {
        this.sizeRepo = sizeRepo;
    }

    @Override
    public Size getSizeById(Long id) {

        return sizeRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Size not found."));
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
        Size size = sizeRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Size not found."));
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
