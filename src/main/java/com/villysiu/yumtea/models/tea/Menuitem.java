package com.villysiu.yumtea.models.tea;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@Table(name="menuitem")
public class Menuitem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "BOOLEAN DEFAULT true" )
    private Boolean active = true;

    @Column(length = 2000) //hold up to 65,535 bytes.
    private String description;

    @Column
    private String imageUrl;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0" )
    private double price = 0.0;

    // belong to a category, if category deleted, this becomes null
    @ManyToOne(optional = true)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne()
    @JoinColumn(name="milk_id")
    private Milk milk ;

//    @Column(length = 4, columnDefinition = "varchar(4) default 'FREE'")
    @Enumerated(EnumType.STRING)
    private Temperature temperature = Temperature.FREE;

//    @Column(length = 12, columnDefinition = "varchar(12) default 'ZERO'")
    @Enumerated(EnumType.STRING)
    private Sugar sugar = Sugar.ZERO;


    public Menuitem(String title, String imageUrl, Category category, Milk milk, Sugar sugar, Temperature temperature, double price) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.milk = milk;
        this.sugar = sugar;
        this.temperature = temperature;
        this.price = price;
    }
}

