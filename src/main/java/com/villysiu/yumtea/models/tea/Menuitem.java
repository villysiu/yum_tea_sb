package com.villysiu.yumtea.models.tea;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString
public class Menuitem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000) //hold up to 65,535 bytes.
    private String description;

    @Column
    private String imageUrl;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private double price;

    // belong to a category, if category deleted, this becomes null
    @ManyToOne(optional = true)
    @JsonBackReference
    @JoinColumn(name = "category_id", columnDefinition = "null")
    private Category category;

    @ManyToOne(optional = true)
    @JoinColumn(name="milk_id", columnDefinition = "BIGINT DEFAULT 12" )
    private Milk milk;

    @Column(length = 4, columnDefinition = "varchar(4) default 'HOT'")
    @Enumerated(EnumType.STRING)
    private Temperature temperature;

    @Column(length = 12, columnDefinition = "varchar(12) default 'ZERO'")
    @Enumerated(EnumType.STRING)
    private Sugar sugar;

}

