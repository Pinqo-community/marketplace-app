package com.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.annotation.ControllerWebMvcTest;
import com.marketplace.dto.ExceptionResponse;
import com.marketplace.dto.category.CategoryCreateDto;
import com.marketplace.dto.category.CategoryResponseDto;
import com.marketplace.dto.category.CategoryUpdateDto;
import com.marketplace.entity.Category;
import com.marketplace.service.CategoryService;
import com.marketplace.utils.mapper.CategoryMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerWebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryMapper categoryMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class GetAllCategories {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            Category category = mock(Category.class);
            CategoryResponseDto categoryResponseDto = mock(CategoryResponseDto.class);

            when(categoryService.findAll()).thenReturn(List.of(category));
            when(categoryMapper.toResponseList(any())).thenReturn(List.of(categoryResponseDto));

            // When
            mockMvc.perform(get("/categories")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class GetCategoryById {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            Category category = mock(Category.class);
            CategoryResponseDto categoryResponseDto = mock(CategoryResponseDto.class);

            when(categoryService.findById(anyLong())).thenReturn(category);
            when(categoryMapper.toResponse(any())).thenReturn(categoryResponseDto);

            // When
            mockMvc.perform(get("/categories/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class CreateCategory {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            CategoryCreateDto request = new CategoryCreateDto(
                    "Name category"
            );

            Category category = mock(Category.class);
            CategoryResponseDto categoryResponseDto = mock(CategoryResponseDto.class);

            when(categoryService.create(any())).thenReturn(category);
            when(categoryMapper.toResponse(any())).thenReturn(categoryResponseDto);

            // When
            mockMvc.perform(post("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        void whenMissingName_thenReturnSuccess() throws Exception {
            // Given
            Category category = mock(Category.class);
            CategoryResponseDto categoryResponseDto = mock(CategoryResponseDto.class);

            when(categoryService.create(any())).thenReturn(category);
            when(categoryMapper.toResponse(any())).thenReturn(categoryResponseDto);

            // When
            MvcResult result = mockMvc.perform(post("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Then
            String contentAsString = result.getResponse().getContentAsString();
            ExceptionResponse actualResponse = objectMapper.readValue(contentAsString, ExceptionResponse.class);

            assertThat(actualResponse)
                    .isNotNull()
                    .isInstanceOf(ExceptionResponse.class);
        }
    }

    @Nested
    class UpdateCategory {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            CategoryUpdateDto request = new CategoryUpdateDto(
                    "Name category"
            );

            Category category = mock(Category.class);
            CategoryResponseDto categoryResponseDto = mock(CategoryResponseDto.class);

            when(categoryService.update(any(), anyLong())).thenReturn(category);
            when(categoryMapper.toResponse(any())).thenReturn(categoryResponseDto);

            // When
            mockMvc.perform(put("/categories/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class DeleteCategory {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // When
            mockMvc.perform(delete("/categories/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }
}