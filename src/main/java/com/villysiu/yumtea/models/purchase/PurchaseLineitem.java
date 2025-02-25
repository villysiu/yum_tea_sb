package com.villysiu.yumtea.models.purchase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.villysiu.yumtea.models.tea.*;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PurchaseLineitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="purchase_id", nullable=false)
    private Purchase purchase;

//    @ManyToOne
//    @JoinColumn(name="doctor_id")
//    private Doctor doctor;

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
    private Temperature temperature;

    @Enumerated(EnumType.STRING)
    private Sugar sugar;

    @Column(columnDefinition = "int default 1", nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double price= 0.0;


}
