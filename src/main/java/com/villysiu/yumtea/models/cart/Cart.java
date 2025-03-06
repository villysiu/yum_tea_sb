package com.villysiu.yumtea.models.cart;

import com.villysiu.yumtea.models.tea.*;
import com.villysiu.yumtea.models.user.Account;
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

    @JoinColumn(name = "milk_id", columnDefinition = "BIGINT DEFAULT 12", nullable=false)
    @ManyToOne(optional = false)
    private Milk milk;

    @JoinColumn(name = "size_id", columnDefinition = "BIGINT DEFAULT 1", nullable=false)
    @ManyToOne(optional = false)
    private Size size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Temperature temperature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sugar sugar;

    @Column(columnDefinition = "int default 1", nullable = false)
    private Integer quantity = 1;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double price = 0.0;

    @PrePersist
    private void setDefaultValue() {
        if (this.sugar == null) {
            this.sugar = Sugar.ZERO;  // Set the default value
        }
        if(this.temperature == null) {
            this.temperature = Temperature.HOT;
        }

        if(this.milk == null) {
            this.milk = new Milk();
            this.milk.setId(12L);
        }
        if(this.size == null) {
            this.size = new Size();
            this.size.setId(1L);
        }
    }

    public Cart(Account account, Menuitem menuitem, Milk milk, Size size) {
        this.account = account;
        this.menuitem = menuitem;
        this.milk = milk;
        this.size = size;
    }
}
