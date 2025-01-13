package com.marketplace.service.impl;

import com.marketplace.dto.category.CategoryCreateDto;
import com.marketplace.dto.category.CategoryUpdateDto;
import com.marketplace.entity.Category;
import com.marketplace.exception.AlreadyExistsException;
import com.marketplace.exception.NotFoundException;

import com.marketplace.repository.CategoryRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Nested
    class FindAll {
        @Test
        void whenValidUser_thenReturnUser() {
            // Given
            List<Category> resultRequest = List.of(mock(Category.class), mock(Category.class));

            when(categoryRepository.findAll()).thenReturn(resultRequest);

            // When
            List<Category> result = categoryService.findAll();

            // Then
            assertNotNull(result);
            assertEquals(resultRequest, result);
        }
    }

    @Nested
    class FindById {
        @Test
        void whenValidUser_thenReturnUser() {
            // Given
            Category resultRequest = mock(Category.class);

            when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(resultRequest));

            // When
            Category result = categoryService.findById(1L);

            // Then
            assertNotNull(result);
            assertEquals(resultRequest, result);
        }

        @Test
        void whenCategoryNotExists_thenThrowException() {
            // Given
            Long id = 1L;

            when(categoryRepository.findById(any()))
                    .thenThrow(new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));

            // When
            NotFoundException result = assertThrows(NotFoundException.class, () -> categoryService.findById(id));

            // Then
            assertEquals("Il n'existe pas de catégorie avec cet ID : " + id, result.getMessage());
        }
    }

    @Nested
    class Create {
        @Test
        void whenValidUser_thenReturnUser() {
            // Given
            CategoryCreateDto request = new CategoryCreateDto(
                    "Name category"
            );

            Category resultRequest = mock(Category.class);

            when(categoryRepository.existsByNameIgnoreCase(any())).thenReturn(false);
            when(categoryRepository.save(any())).thenReturn(resultRequest);

            // When
            Category result = categoryService.create(request);

            // Then
            assertNotNull(result);
            assertEquals(resultRequest, result);
        }

        @Test
        void whenCategoryExists_thenThrowException() {
            // Given
            CategoryCreateDto request = new CategoryCreateDto(
                    "Name category"
            );

            when(categoryRepository.existsByNameIgnoreCase(any())).thenReturn(true);

            // When
            AlreadyExistsException result = assertThrows(AlreadyExistsException.class, () -> categoryService.create(request));

            // Then
            assertEquals("Il existe déja une catégorie avec ce nom : " + request.name(), result.getMessage());
        }
    }

    @Nested
    class Update {
        @Test
        void whenValidUser_thenReturnUser() {
            // Given
            CategoryUpdateDto request = new CategoryUpdateDto(
                    "Name category"
            );

            Category resultRequest = mock(Category.class);

            when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(resultRequest));
            when(categoryRepository.save(any())).thenReturn(resultRequest);

            // When
            Category result = categoryService.update(request, 1L);

            // Then
            assertNotNull(result);
            assertEquals(resultRequest, result);
        }

        @Test
        void whenCategoryNotExists_thenThrowException() {
            // Given
            Long id = 1L;
            CategoryUpdateDto request = new CategoryUpdateDto(
                    "Name category"
            );

            when(categoryRepository.findById(any()))
                    .thenThrow(new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));

            // When
            NotFoundException result = assertThrows(NotFoundException.class, () -> categoryService.update(request, id));

            // Then
            assertEquals("Il n'existe pas de catégorie avec cet ID : " + id, result.getMessage());
        }
    }

    @Nested
    class DeleteById {
        @Test
        void whenValidUser_thenReturnUser() {
            // Given
            Category resultRequest = mock(Category.class);

            when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(resultRequest));

            // When
            assertDoesNotThrow(() -> categoryService.deleteById(1L));
        }

        @Test
        void whenCategoryNotExists_thenThrowException() {
            // Given
            Long id = 1L;

            when(categoryRepository.findById(any()))
                    .thenThrow(new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));

            // When
            NotFoundException result = assertThrows(NotFoundException.class, () -> categoryService.deleteById(id));

            // Then
            assertEquals("Il n'existe pas de catégorie avec cet ID : " + id, result.getMessage());
        }
    }
}