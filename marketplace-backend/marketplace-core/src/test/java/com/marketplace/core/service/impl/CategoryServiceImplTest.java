package com.marketplace.core.service.impl;

import com.marketplace.api.dto.category.CategoryCreateDto;
import com.marketplace.api.dto.category.CategoryResponseDto;
import com.marketplace.api.dto.category.CategoryUpdateDto;
import com.marketplace.api.exception.AlreadyExistsException;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.core.entity.Category;
import com.marketplace.core.repository.CategoryRepository;
import com.marketplace.core.utils.mapper.CategoryMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Nested
    class FindAll {
        @Test
        void whenFindAll_thenReturnDtoList() {
            // Given
            List<Category> categories = List.of(
                    Category.builder().id(1L).name("Category 1").build(),
                    Category.builder().id(2L).name("Category 2").build()
            );
            List<CategoryResponseDto> expectedDtos = List.of(
                    new CategoryResponseDto(1L, "Category 1"),
                    new CategoryResponseDto(2L, "Category 2")
            );

            when(categoryRepository.findAll()).thenReturn(categories);
            when(categoryMapper.toResponseList(categories)).thenReturn(expectedDtos);

            // When
            List<CategoryResponseDto> result = categoryService.findAll();

            // Then
            assertThat(result).isEqualTo(expectedDtos);
            verify(categoryRepository).findAll();
            verify(categoryMapper).toResponseList(categories);
        }
    }

    @Nested
    class FindById {
        @Test
        void whenValidId_thenReturnDto() {
            // Given
            Long id = 1L;
            Category category = Category.builder().id(id).name("Category 1").build();
            CategoryResponseDto expectedDto = new CategoryResponseDto(id, "Category 1");

            when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
            when(categoryMapper.toResponse(category)).thenReturn(expectedDto);

            // When
            CategoryResponseDto result = categoryService.findById(id);

            // Then
            assertThat(result).isEqualTo(expectedDto);
            verify(categoryRepository).findById(id);
            verify(categoryMapper).toResponse(category);
        }

        @Test
        void whenInvalidId_thenThrowNotFoundException() {
            // Given
            Long id = 1L;
            when(categoryRepository.findById(id)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> categoryService.findById(id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Il n'existe pas de catégorie avec cet ID : " + id);
        }
    }

    @Nested
    class Create {
        @Test
        void whenValidData_thenReturnDto() {
            // Given
            CategoryCreateDto createDto = new CategoryCreateDto("New Category");
            Category savedCategory = Category.builder().id(1L).name("New Category").build();
            CategoryResponseDto expectedDto = new CategoryResponseDto(1L, "New Category");

            when(categoryRepository.existsByNameIgnoreCase(createDto.name())).thenReturn(false);
            when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
            when(categoryMapper.toResponse(savedCategory)).thenReturn(expectedDto);

            // When
            CategoryResponseDto result = categoryService.create(createDto);

            // Then
            assertThat(result).isEqualTo(expectedDto);
            verify(categoryRepository).existsByNameIgnoreCase(createDto.name());
            verify(categoryRepository).save(any(Category.class));
            verify(categoryMapper).toResponse(savedCategory);
        }

        @Test
        void whenNameExists_thenThrowAlreadyExistsException() {
            // Given
            CategoryCreateDto createDto = new CategoryCreateDto("Existing Category");
            when(categoryRepository.existsByNameIgnoreCase(createDto.name())).thenReturn(true);

            // When/Then
            assertThatThrownBy(() -> categoryService.create(createDto))
                    .isInstanceOf(AlreadyExistsException.class)
                    .hasMessage("Il existe déja une catégorie avec ce nom : " + createDto.name());
        }
    }

    @Nested
    class Update {
        @Test
        void whenValidData_thenReturnDto() {
            // Given
            Long id = 1L;
            CategoryUpdateDto updateDto = new CategoryUpdateDto("Updated Category");
            Category existingCategory = Category.builder().id(id).name("Old Category").build();
            Category updatedCategory = Category.builder().id(id).name("Updated Category").build();
            CategoryResponseDto expectedDto = new CategoryResponseDto(id, "Updated Category");

            when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
            when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
            when(categoryMapper.toResponse(updatedCategory)).thenReturn(expectedDto);

            // When
            CategoryResponseDto result = categoryService.update(updateDto, id);

            // Then
            assertThat(result).isEqualTo(expectedDto);
            verify(categoryRepository).findById(id);
            verify(categoryRepository).save(any(Category.class));
            verify(categoryMapper).toResponse(updatedCategory);
        }

        @Test
        void whenInvalidId_thenThrowNotFoundException() {
            // Given
            Long id = 1L;
            CategoryUpdateDto updateDto = new CategoryUpdateDto("Updated Category");
            when(categoryRepository.findById(id)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> categoryService.update(updateDto, id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Il n'existe pas de catégorie avec cet ID : " + id);
        }
    }

    @Nested
    class DeleteById {
        @Test
        void whenValidId_thenDeleteSuccessfully() {
            // Given
            Long id = 1L;
            Category category = Category.builder().id(id).name("Category").build();
            when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

            // When
            categoryService.deleteById(id);

            // Then
            verify(categoryRepository).findById(id);
            verify(categoryRepository).deleteById(id);
        }

        @Test
        void whenInvalidId_thenThrowNotFoundException() {
            // Given
            Long id = 1L;
            when(categoryRepository.findById(id)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> categoryService.deleteById(id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Il n'existe pas de catégorie avec cet ID : " + id);
        }
    }
}