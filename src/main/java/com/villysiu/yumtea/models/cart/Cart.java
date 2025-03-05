package com.villysiu.yumtea.models.cart;

import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.models.user.Account;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(name="UniqueCartAndUser", columnNames = {"user_id", "menuitem_id", "milk_id", "size_id","temperature", "sugar"})
}) //java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '2-6-3-1-HOT-FIFTY' for key 'cart.UniqueCartAndUser'

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;


    @JoinColumn(name = "menuitem_id", nullable = false)
    @ManyToOne(optional = false)
    private Menuitem menuitem;

    @JoinColumn(name = "milk_id", nullable = false)
    @ManyToOne(optional = false)
    private Milk milk;

    @JoinColumn(name = "size_id", nullable = false)
    @ManyToOne(optional = false)
    private Size size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Temperature temperature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sugar sugar;

    @Column(columnDefinition = "int default 1", nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double price = 0.0;
}
