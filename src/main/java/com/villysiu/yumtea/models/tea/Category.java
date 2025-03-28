package com.villysiu.yumtea.models.tea;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String imageUrl;

    public Category(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }
    public Category(String title) {
        this.title = title;

    }
}
