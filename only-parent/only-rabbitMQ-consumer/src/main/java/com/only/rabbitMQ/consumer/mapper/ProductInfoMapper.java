package com.only.rabbitMQ.consumer.mapper;

import com.only.rabbitMQ.consumer.entity.ProductInfo;
import org.apache.ibatis.annotations.Param;

public interface ProductInfoMapper {
	int deleteByPrimaryKey(Integer productNo);

	int insert(ProductInfo record);

	int insertSelective(ProductInfo record);

	ProductInfo selectByPrimaryKey(Integer productNo);

	int updateByPrimaryKeySelective(ProductInfo record);

	int updateByPrimaryKey(ProductInfo record);

	int updateProductStoreById(@Param("productId") Integer productId);
}
