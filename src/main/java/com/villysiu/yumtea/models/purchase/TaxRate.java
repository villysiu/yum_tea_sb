package com.villysiu.yumtea.models.purchase;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table
@Entity
@Data
@NoArgsConstructor
public class TaxRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String state;

    @Column(nullable = false, columnDefinition = "double default 0" )
    private Double rate;

    public TaxRate(String state, Double rate) {
        this.state = state;
        this.rate = rate;
    }
}
