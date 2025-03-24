package com.villysiu.yumtea.service.query;

import com.villysiu.yumtea.dto.response.BestSellerDto;

import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomQueryService {
    private final PurchaseLineitemRepo purchaseLineitemRepo;

    public CustomQueryService(PurchaseLineitemRepo purchaseLineitemRepo) {
        this.purchaseLineitemRepo = purchaseLineitemRepo;
    }


    public List<BestSellerDto> getSoldCountByMenuitem(){
        List<Object[]> res = purchaseLineitemRepo.findSoldCountByMenuitem();
        List<BestSellerDto> bestSellerDtos = new ArrayList<>();
        for(Object[] row : res) {
            BestSellerDto bestSellerDto = new BestSellerDto();
            bestSellerDto.setMenuitemId((Long) row[0]);
            bestSellerDto.setMenuitemTitle((String) row[1]);
            bestSellerDto.setCount((Long) row[2]);
            bestSellerDtos.add(bestSellerDto);
        }
        return bestSellerDtos;

    }
    public List<Object[]> getMilkByPurchaseLineitem(){
        return purchaseLineitemRepo.findPopularMilk();
    }
}
