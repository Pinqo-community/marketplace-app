package com.marketplace.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between Product and ProductDto objects.
 */
@Mapper
public interface ProductMapper {
    /**
     * Instance of ProductMapper used to access the mapper.
     */
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

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
}

