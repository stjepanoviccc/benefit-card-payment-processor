package com.example.bcpp.model;

import com.example.bcpp.model.enums.MerchantCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "`merchant`")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchantCategory category;

    @Column(nullable = false)
    private Double price;

    @JsonIgnore
    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    private List<CompanyMerchant> companyMerchants;
}
