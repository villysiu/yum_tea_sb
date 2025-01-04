package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.tea.CartLineitemDto;
import com.villysiu.yumtea.models.CartLineitem;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.CartLineitemRepo;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.repo.tea.SizeRepo;
import com.villysiu.yumtea.repo.user.UserRepo;
import com.villysiu.yumtea.service.CartLineitemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartLineitemServiceImpl implements CartLineitemService {
    private final CartLineitemRepo cartLineitemRepo;
    private final MenuitemRepo menuitemRepo;
    private final MilkRepo milkRepo;
    private final SizeRepo sizeRepo;
    private final UserRepo userRepo;

    @Override
    public CartLineitem createCartLineitem(CartLineitemDto cartLineitemDto) throws RuntimeException {
        User user = userRepo.findById(cartLineitemDto.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Menuitem menuitem = menuitemRepo.findById(cartLineitemDto.getMenuitemId())
                .orElseThrow(()-> new RuntimeException("Menuitem not found."));
        Milk milk = milkRepo.findById(cartLineitemDto.getMilkId())
                .orElseThrow(()->new RuntimeException("Milk not found."));
        Size size = sizeRepo.findById(cartLineitemDto.getSizeId())
                .orElseThrow(()->new RuntimeException("Size not found."));

//        System.out.println(menuitem);
        CartLineitem cartLineitem = new CartLineitem();

        cartLineitem.setUser(user);
        cartLineitem.setMenuitem(menuitem);


        cartLineitem.setMilk(milk);
        cartLineitem.setSize(size);

        cartLineitem.setPrice(cartLineitemDto.getPrice());
        cartLineitem.setQuantity(cartLineitemDto.getQuantity());
        cartLineitem.setTemperature(cartLineitemDto.getTemperature());
        cartLineitem.setSugar(cartLineitemDto.getSugar());

        System.out.println(cartLineitem.toString());
        cartLineitemRepo.save(cartLineitem);
        return cartLineitem;
//        return null;
    }
}
