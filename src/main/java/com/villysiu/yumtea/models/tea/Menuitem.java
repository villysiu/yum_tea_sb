package com.villysiu.yumtea.models.tea;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Menuitem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
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
    @JoinColumn(name="milk_id", columnDefinition = "null")
    private Milk milk;

    @Enumerated(EnumType.STRING)
    private Temperature temperature;

    @Enumerated(EnumType.STRING)
    private Sugar sugar;

}

