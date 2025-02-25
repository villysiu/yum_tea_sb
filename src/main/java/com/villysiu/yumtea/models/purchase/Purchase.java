package com.villysiu.yumtea.models.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.villysiu.yumtea.models.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double tip = 0.0;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double tax = 0.0;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double total = 0.0;

//    @JsonManagedReference
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PurchaseLineitem> purchaseLineitemList;

//    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//   private List<Appointment> appointments = new ArrayList<>();
}
