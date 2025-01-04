package com.villysiu.yumtea.models.cart;

import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.models.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @JoinColumn(name = "menuitem_id")
    @ManyToOne(optional = false)
    private Menuitem menuitem;

    @JoinColumn(name = "milk_id")
    @ManyToOne(optional = false)
    private Milk milk;

    @JoinColumn(name = "size_id")
    @ManyToOne(optional = false)
    private Size size;

    @Enumerated(EnumType.STRING)
    private Temperature temperature;

    @Enumerated(EnumType.STRING)
    private Sugar sugar;

    @Column(columnDefinition = "int default 1", nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "double default 0", nullable = false)
    private Double price;
}
