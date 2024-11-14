package com.marketplace.utils.mapper;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "nutritionalValue", source = "nutritionalValue")
    @Mapping(target = "listOfIngredients", source = "listOfIngredients")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    @Mapping(target = "criticalLevel", source = "criticalLevel")
    ProductDto toDto(Product product);

    /**
     * Converts a ProductDto to a Product entity.
     *
     * @param productDto The ProductDto to be converted.
     * @return The converted Product entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "nutritionalValue", source = "nutritionalValue")
    @Mapping(target = "listOfIngredients", source = "listOfIngredients")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    @Mapping(target = "criticalLevel", source = "criticalLevel")
    Product toEntity(ProductDto productDto);
}

