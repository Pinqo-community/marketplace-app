package com.marketplace.utils.mapper;

import com.marketplace.dto.category.CategoryCreateDto;
import com.marketplace.dto.category.CategoryResponseDto;
import com.marketplace.dto.category.CategoryUpdateDto;
import com.marketplace.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryCreateDto dto);

    CategoryResponseDto toResponse(Category category);

    List<CategoryResponseDto> toResponseList(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(CategoryUpdateDto categoryUpdateDto, @MappingTarget Category category);
}
