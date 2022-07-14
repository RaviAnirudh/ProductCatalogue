package com.api.Entity;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductPKID implements Serializable {
    private String productName;

    private Long merchantId;

}
