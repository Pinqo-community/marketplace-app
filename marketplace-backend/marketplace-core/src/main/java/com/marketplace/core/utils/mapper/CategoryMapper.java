package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.category.CategoryResponseDto;
import com.marketplace.core.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper interface for converting between Category entities and DTOs.
 * Uses MapStruct for automatic mapping implementation.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    /**
     * Converts a Category entity to a response DTO.
     *
     * @param category the category entity to convert
     * @return the converted category response DTO
     */
    CategoryResponseDto toResponse(Category category);

    /**
     * Converts a list of Category entities to response DTOs.
     *
     * @param categories list of category entities to convert
     * @return list of converted category response DTOs
     */
    List<CategoryResponseDto> toResponseList(List<Category> categories);
}
