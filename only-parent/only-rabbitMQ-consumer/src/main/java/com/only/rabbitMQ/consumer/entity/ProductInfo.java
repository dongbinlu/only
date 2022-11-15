package com.only.rabbitMQ.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
	private Integer productNo;

	private String productName;

	private Integer productNum;
}
