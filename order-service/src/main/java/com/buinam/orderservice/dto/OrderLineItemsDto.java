package com.buinam.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderLineItemsDto {
    private Long id;
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;
}
