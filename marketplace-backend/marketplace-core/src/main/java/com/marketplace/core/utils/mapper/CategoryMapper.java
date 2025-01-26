package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.category.CategoryResponseDto;
import com.marketplace.core.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toResponse(Category category);

    List<CategoryResponseDto> toResponseList(List<Category> categories);
}
