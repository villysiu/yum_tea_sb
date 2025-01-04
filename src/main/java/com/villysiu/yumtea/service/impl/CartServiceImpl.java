package com.villysiu.yumtea.service.impl;

import com.villysiu.yumtea.dto.input.CartInputDto;
import com.villysiu.yumtea.dto.output.CartOutputDto;
import com.villysiu.yumtea.models.cart.Cart;
import com.villysiu.yumtea.models.tea.Menuitem;
import com.villysiu.yumtea.models.tea.Milk;
import com.villysiu.yumtea.models.tea.Size;
import com.villysiu.yumtea.models.user.User;
import com.villysiu.yumtea.repo.cart.CartRepo;
import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import com.villysiu.yumtea.repo.tea.SizeRepo;

import com.villysiu.yumtea.service.CartService;
import com.villysiu.yumtea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final MenuitemRepo menuitemRepo;
    private final MilkRepo milkRepo;
    private final SizeRepo sizeRepo;
//    private final UserRepo userRepo;
    private final UserService userService;

    @Override
    public CartOutputDto createCart(CartInputDto cartInoutDto) throws RuntimeException {
        Cart cart = new Cart();
        User user = userService.getCurrentUser();

        Menuitem menuitem = menuitemRepo.findById(cartInoutDto.getMenuitemId())
                .orElseThrow(()-> new RuntimeException("Menuitem not found."));
        Milk milk = milkRepo.findById(cartInoutDto.getMilkId())
                .orElseThrow(()->new RuntimeException("Milk not found."));
        Size size = sizeRepo.findById(cartInoutDto.getSizeId())
                .orElseThrow(()->new RuntimeException("Size not found."));

        cart.setUser(user);
        cart.setMenuitem(menuitem);

        cart.setMilk(milk);
        cart.setSize(size);

        cart.setPrice(cartInoutDto.getPrice());
        cart.setQuantity(cartInoutDto.getQuantity());
        cart.setTemperature(cartInoutDto.getTemperature());
        cart.setSugar(cartInoutDto.getSugar());
        System.out.println(cart);
        cartRepo.save(cart);

        return helper(cart);
    }

    @Override
    public CartOutputDto updateCart(Long id, CartInputDto cartInputDto) throws RuntimeException {
        Cart cart = cartRepo.findById(id).orElseThrow(()->new RuntimeException("Cart not found."));

        User user = userService.getCurrentUser();

        System.out.println(user);

        Menuitem menuitem = menuitemRepo.findById(cartInputDto.getMenuitemId())
                .orElseThrow(()-> new RuntimeException("Menuitem not found."));
        Milk milk = milkRepo.findById(cartInputDto.getMilkId())
                .orElseThrow(()->new RuntimeException("Milk not found."));
        Size size = sizeRepo.findById(cartInputDto.getSizeId())
                .orElseThrow(()->new RuntimeException("Size not found."));

        cart.setUser(user);
        cart.setMenuitem(menuitem);

        cart.setMilk(milk);
        cart.setSize(size);

        cart.setPrice(cartInputDto.getPrice());
        cart.setQuantity(cartInputDto.getQuantity());
        cart.setTemperature(cartInputDto.getTemperature());
        cart.setSugar(cartInputDto.getSugar());

        cartRepo.save(cart);

        return helper(cart);
    }

    private CartOutputDto helper(Cart data ){
        CartOutputDto dest = new CartOutputDto();
        dest.setId(data.getId());
        dest.setMenuitemId(data.getMenuitem().getId());
        dest.setMenuitemTitle(data.getMenuitem().getTitle());

        dest.setMilkId(data.getMilk().getId());
        dest.setMilkTitle(data.getMilk().getTitle());

        dest.setSizeId(data.getSize().getId());
        dest.setSizeTitle(data.getSize().getTitle());

        dest.setQuantity(data.getQuantity());
        dest.setPrice(data.getPrice());
        dest.setSugar(data.getSugar());
        dest.setTemperature(data.getTemperature());

        return dest;
    }
}
