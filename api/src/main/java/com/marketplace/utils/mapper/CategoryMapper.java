package com.marketplace.utils.mapper;

import com.marketplace.dto.category.CategoryResponseDto;
import com.marketplace.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toResponse(Category category);

    List<CategoryResponseDto> toResponseList(List<Category> categories);
}
