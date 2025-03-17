package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.product.ProductDto;
import com.marketplace.core.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * Mapper interface for converting between Product and ProductDto objects.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Converts a Product entity to a ProductDto.
     *
     * @param product The Product entity to be converted.
     * @return The converted ProductDto.
     */
    ProductDto toDto(Product product);

    /**
     * Converts a ProductDto to a Product entity.
     *
     * @param productDto The ProductDto to be converted.
     * @return The converted Product entity.
     */
    Product toEntity(ProductDto productDto);

    /**
     * Converts a list of product entities to a list of ProductDto
     * @param products The Products entity to be converted.
     * @return The converted ProductDtoList.
     */
    List<ProductDto> toDtoList(List<Product> products);
}

