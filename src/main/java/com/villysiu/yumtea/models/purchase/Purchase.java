package com.villysiu.yumtea.models.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.villysiu.yumtea.models.user.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")  //ensures database has a current date
    @JsonFormat(pattern = "yyyy-MM-dd")  //ensure date is return in restAPI as "yyyy-MM-dd"
    private Date purchaseDate = new Date(); //ensures JAVA object has a date when initialized

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double tip = 0.0;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double tax = 0.0;

    @Column(columnDefinition = "double default 0.0", nullable = false)
    private Double total = 0.0;

//    @JsonManagedReference
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PurchaseLineitem> purchaseLineitemList;

    public Purchase(Account account) {
        this.account = account;
    }
}
