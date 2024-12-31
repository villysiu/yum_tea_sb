package com.villysiu.yumtea.models.tea;

import jakarta.persistence.*;

@Entity
public class Milk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="milk_id")
    private Long id;

    @Column(name="milk_name", nullable = false)
    private String title;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
//    @ColumnDefault("0.00")
    private double price;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
