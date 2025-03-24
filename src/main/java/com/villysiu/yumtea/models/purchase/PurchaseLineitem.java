package com.villysiu.yumtea.models.purchase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.villysiu.yumtea.models.tea.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PurchaseLineitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="purchase_id", nullable=false)
    private Purchase purchase;


    @ManyToOne
    @JoinColumn(name="menuitem_id", nullable=false)
    private Menuitem menuitem;

    @ManyToOne
    @JoinColumn(name="milk_id", nullable=false)
    private Milk milk;

    @ManyToOne
    @JoinColumn(name="size_id", nullable=false)
    private Size size;

    @Enumerated(EnumType.STRING)
    private Temperature temperature = Temperature.FREE;

    @Enumerated(EnumType.STRING)
    private Sugar sugar = Sugar.ZERO;

    @Column(columnDefinition = "int default 1", nullable = false)
    private Integer quantity = 1;

    @Column(columnDefinition = "double default 0.0", nullable = false) //ensure database defult to 0.0
    private Double price = 0.0;



    public PurchaseLineitem(Purchase purchase, Menuitem menuitem, Milk milk, Size size) {
        this.purchase = purchase;
        this.menuitem = menuitem;
        this.milk = milk;
        this.size = size;
    }
}
