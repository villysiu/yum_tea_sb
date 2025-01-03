package com.villysiu.yumtea.models.tea;

import com.villysiu.yumtea.models.user.Role;
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

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName="id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="milk_id")
    private Milk milk;

    @Enumerated(EnumType.STRING)
    private Temperature temperature;


//    @Enumerated(EnumType.STRING)
//    private Sweetness sweetness;



}

