package com.learning.yasminishop.product.mapper;

import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.ProductAttribute;
import com.learning.yasminishop.common.entity.ProductAttributeValue;
import com.learning.yasminishop.product.dto.request.ProductAttributeRequest;
import com.learning.yasminishop.product.dto.request.ProductRequest;
import com.learning.yasminishop.product.dto.response.ProductAdminResponse;
import com.learning.yasminishop.product.dto.response.ProductAttributeResponse;
import com.learning.yasminishop.product.dto.response.ProductAttributeValueResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(ProductRequest productCreation);
    ProductResponse toProductResponse(Product product);

    ProductAttribute toProductAttribute(ProductAttributeRequest productAttributeRequest);
    ProductAttributeValue toProductAttributeValue(ProductAttributeRequest productAttributeRequest);

    ProductAttributeValueResponse toProductAttributeValueResponse(ProductAttributeValue productAttributeValue);
    ProductAttributeResponse toProductAttributeResponse(ProductAttribute productAttribute);

    ProductAdminResponse toProductAdminResponse(Product product);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductRequest productUpdate);
}
