package com.learning.yasminishop.product.mapper;

import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.product.dto.request.ProductCreation;
import com.learning.yasminishop.product.dto.response.ProductAdminResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductCreation productCreation);
    ProductResponse toProductResponse(Product product);

    ProductAdminResponse toProductAdminResponse(Product product);
}
