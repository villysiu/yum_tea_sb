package com.villysiu.yumtea.service.query;

import com.villysiu.yumtea.dto.response.BestSellerDto;

import com.villysiu.yumtea.repo.purchase.PurchaseLineitemRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomQueryService {
    private final PurchaseLineitemRepo purchaseLineitemRepo;

    public CustomQueryService(PurchaseLineitemRepo purchaseLineitemRepo) {
        this.purchaseLineitemRepo = purchaseLineitemRepo;
    }


    public List<BestSellerDto> getAllSales(){
        List<Object[]> res = purchaseLineitemRepo.findAllSalesByMenuitem();
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

    public List<BestSellerDto> getMonthlyBestSellers(){
//        Pageable pageable = PageRequest.of(0, 3);
//        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
//
//        List<Object[]> res = purchaseLineitemRepo.findMonthlyBestSellers(thirtyDaysAgo, pageable);
//        System.out.println(res);
//        List<BestSellerDto> bestSellerDtos = new ArrayList<>();
//        for(Object[] row : res) {
//            BestSellerDto bestSellerDto = new BestSellerDto();
//            bestSellerDto.setMenuitemId((Long) row[0]);
//            bestSellerDto.setMenuitemTitle((String) row[1]);
//            bestSellerDto.setCount((Long) row[2]);
//            bestSellerDtos.add(bestSellerDto);
//        }
//        return bestSellerDtos;
        List<Object[]> res = purchaseLineitemRepo.findAllSalesByMenuitem();

        List<BestSellerDto> bestSellerDtos = new ArrayList<>();
        for(Object[] row : res.subList(0,3)) {
            BestSellerDto bestSellerDto = new BestSellerDto();
            bestSellerDto.setMenuitemId((Long) row[0]);
            bestSellerDto.setMenuitemTitle((String) row[1]);
            bestSellerDto.setCount((Long) row[2]);
            bestSellerDtos.add(bestSellerDto);
        }
        return bestSellerDtos;
    }

}
