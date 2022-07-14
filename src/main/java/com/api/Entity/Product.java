package com.api.Entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
//@Where(clause="is_active=1")
@IdClass(ProductPKID.class)
public class Product {

    @Column(name = "productId", updatable = false, nullable = false)
    private String productId;

    @PrePersist
    public void initializeProductId() {
        if (productId == null) {
            productId = UUID.randomUUID().toString();
        }
    }
    @Id
    private Long merchantId;

    private Long productQuantity;

    @Id
    private String productName;

    private String productDescription;

    private String productCategory;

    private Double productPrice;

    @CreationTimestamp
    @Column(name = "created", updatable = false)
    private LocalDate created;

    @UpdateTimestamp
    private LocalDate modified;

    @Column(name="is_active")
    private Boolean active = true;


}
