package com.villysiu.yumtea.models.tea;

import jakarta.persistence.*;

@Entity
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
//    @ColumnDefault("0.00")
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Milk getMilk() {
        return milk;
    }

    public void setMilk(Milk milk) {
        this.milk = milk;
    }
}

