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
    @JoinColumn(name="milk_id", columnDefinition = "BIGINT DEFAULT 12" )
    private Milk milk;

//    @Column(length = 4, columnDefinition = "varchar(4) default 'FREE'")
    @Enumerated(EnumType.STRING)
    private Temperature temperature;

//    @Column(length = 12, columnDefinition = "varchar(12) default 'ZERO'")
    @Enumerated(EnumType.STRING)
    private Sugar sugar;

    @PrePersist
    private void setDefaultValue() {
        if (this.sugar == null) {
            this.sugar = Sugar.ZERO;  // Set the default value
        }
        if(this.temperature == null) {
            this.temperature = Temperature.FREE;
        }
        if(this.milk == null) {
            this.milk = new Milk();
            this.milk.setId(12L);
        }


    }

    public Menuitem(String title, Category category, Milk milk) {
        this.title = title;
        this.category = category;
        this.milk = milk;
    }
}

