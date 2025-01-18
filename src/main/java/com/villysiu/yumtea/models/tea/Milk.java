package com.villysiu.yumtea.models.tea;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name="milk")
public class Milk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private double price;


//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
}
