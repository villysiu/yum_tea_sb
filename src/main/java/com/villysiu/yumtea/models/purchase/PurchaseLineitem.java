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
    @JoinColumn(name="milk_id", columnDefinition = "BIGINT DEFAULT 12", nullable=false)
    private Milk milk;

    @ManyToOne
    @JoinColumn(name="size_id", columnDefinition = "BIGINT DEFAULT 1", nullable=false)
    private Size size;

    @Enumerated(EnumType.STRING)
    private Temperature temperature;

    @Enumerated(EnumType.STRING)
    private Sugar sugar;

    @Column(columnDefinition = "int default 1", nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "double default 0.0", nullable = false) //ensure database defult to 0.0
    private Double price= 0.0;

    // ensures the POJO has a default value when nothing is privided in constructor
    @PrePersist
    private void setDefaultValue() {
        if (this.sugar == null) {
            this.sugar = Sugar.ZERO;  // Set the default value
        }
        if(this.temperature == null) {
            this.temperature = Temperature.HOT;
        }
        if(this.quantity == null) {
            this.quantity = 1;
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

    public PurchaseLineitem(Purchase purchase, Menuitem menuitem, Milk milk, Size size) {
        this.purchase = purchase;
        this.menuitem = menuitem;
        this.milk = milk;
        this.size = size;
    }
}
