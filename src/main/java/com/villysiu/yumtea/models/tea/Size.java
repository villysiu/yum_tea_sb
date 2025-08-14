package com.villysiu.yumtea.models.tea;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "size")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(columnDefinition = "Double default 0.0")
    private Double price = 0.0;

    public Size(String title) {
        this.title = title;
    }

    public Size(String title, Double price) {
        this.title = title;
        this.price = price;
    }

    // public void setTitle(String title) {
    // this.title = title;
    // }
    // public void setPrice(Double price) {
    // this.price = price;
    // }
}
