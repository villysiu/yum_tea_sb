package com.villysiu.yumtea.models.tea;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="milk")
public class Milk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private double price=0.0;

    public Milk(String title) {
        this.title = title;
    }
}
