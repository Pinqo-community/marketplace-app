package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.category.CategoryResponseDto;
import com.marketplace.core.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryMapperTest {
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = Mappers.getMapper(CategoryMapper.class);
    }

    @Test
    void toResponse_whenValidCategory_thenMapAllFields() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        // When
        CategoryResponseDto result = categoryMapper.toResponse(category);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(category.getId());
        assertThat(result.name()).isEqualTo(category.getName());
    }

    @Test
    void toResponse_whenNullCategory_thenReturnNull() {
        // When
        CategoryResponseDto result = categoryMapper.toResponse(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void toResponseList_whenValidCategories_thenMapAllCategories() {
        // Given
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        List<Category> categories = Arrays.asList(category1, category2);

        // When
        List<CategoryResponseDto> result = categoryMapper.toResponseList(categories);

        // Then
        assertThat(result).isNotNull()
                .hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(category1.getId());
        assertThat(result.get(0).name()).isEqualTo(category1.getName());
        assertThat(result.get(1).id()).isEqualTo(category2.getId());
        assertThat(result.get(1).name()).isEqualTo(category2.getName());
    }

    @Test
    void toResponseList_whenNullList_thenReturnNull() {
        // When
        List<CategoryResponseDto> result = categoryMapper.toResponseList(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void toResponseList_whenEmptyList_thenReturnEmptyList() {
        // When
        List<CategoryResponseDto> result = categoryMapper.toResponseList(List.of());

        // Then
        assertThat(result).isNotNull()
                .isEmpty();
    }
}