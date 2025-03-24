package com.villysiu.yumtea.controller.data;

import com.villysiu.yumtea.dto.response.BestSellerDto;
import com.villysiu.yumtea.service.query.CustomQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    private final CustomQueryService customQueryService;

    public DataController(CustomQueryService customQueryService) {
        this.customQueryService = customQueryService;

    }
    @GetMapping("/salesByMenuitem/{count}")
    public List<BestSellerDto> getBestSellers(@PathVariable("count") int count){
        List<BestSellerDto> bestSellerDtoList = customQueryService.getSoldCountByMenuitem();
        if(count == 0)
            return bestSellerDtoList;
        return bestSellerDtoList.subList(0, Math.min(count, bestSellerDtoList.size()));
    }
    @GetMapping("/milk")
    public List<Object[]> getSalesByMilk(){
        return customQueryService.getMilkByPurchaseLineitem();

    }
}
