package com.villysiu.yumtea.models.cart;

import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.models.user.Account;
import com.villysiu.yumtea.repo.tea.MilkRepo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name="UniqueCartAndAccount", columnNames = {"account_id", "menuitem_id", "milk_id", "size_id","temperature", "sugar"})
}) //java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '2-6-3-1-HOT-FIFTY' for key 'cart.UniqueCartAndUser'

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;


    @JoinColumn(name = "menuitem_id", nullable = false)
    @ManyToOne(optional = false)
    private Menuitem menuitem;

    @JoinColumn(name = "milk_id", nullable=false)
    @ManyToOne(optional = false)
    private Milk milk;

    @JoinColumn(name = "size_id", columnDefinition = "BIGINT DEFAULT 1", nullable=false)
    @ManyToOne(optional = false)
    private Size size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Temperature temperature = Temperature.HOT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sugar sugar = Sugar.ZERO;

    @Column(columnDefinition = "int default 1", nullable = false)
    private Integer quantity = 1;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double price = 0.0;


    public Cart(Account account, Menuitem menuitem, Milk milk, Size size) {
        this.account = account;
        this.menuitem = menuitem;
        this.milk = milk;
        this.size = size;
    }
}
