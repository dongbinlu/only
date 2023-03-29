package com.safecode.security.order.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PriceInfo {

    private Long id;

    private BigDecimal price;

}
