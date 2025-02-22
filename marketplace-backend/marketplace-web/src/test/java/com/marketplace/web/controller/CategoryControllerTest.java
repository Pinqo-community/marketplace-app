package com.marketplace.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.api.dto.category.CategoryCreateDto;
import com.marketplace.api.dto.category.CategoryResponseDto;
import com.marketplace.api.dto.category.CategoryUpdateDto;
import com.marketplace.api.dto.exception.ExceptionResponse;
import com.marketplace.api.service.CategoryService;
import com.marketplace.web.configuration.WebMvcBaseTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@ContextConfiguration(classes = CategoryController.class)
class CategoryControllerTest extends WebMvcBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class GetAllCategories {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Test Category");
            when(categoryService.findAll()).thenReturn(List.of(categoryResponseDto));

            // When & Then
            mockMvc.perform(get("/categories")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(List.of(categoryResponseDto))));
        }
    }

    @Nested
    class GetCategoryById {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Test Category");
            when(categoryService.findById(anyLong())).thenReturn(categoryResponseDto);

            // When & Then
            mockMvc.perform(get("/categories/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(categoryResponseDto)));
        }
    }

    @Nested
    class CreateCategory {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            CategoryCreateDto request = new CategoryCreateDto("Test Category");
            CategoryResponseDto response = new CategoryResponseDto(1L, "Test Category");

            when(categoryService.create(any())).thenReturn(response);

            // When & Then
            mockMvc.perform(post("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }

        @Test
        void whenMissingName_thenReturnBadRequest() throws Exception {
            // Given
            String requestBody = "{}";

            // When & Then
            mockMvc.perform(post("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        assertThat(result.getResolvedException())
                                .isInstanceOf(MethodArgumentNotValidException.class);
                    });
        }
    }

    @Nested
    class UpdateCategory {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            CategoryUpdateDto request = new CategoryUpdateDto("Updated Category");
            CategoryResponseDto response = new CategoryResponseDto(1L, "Updated Category");

            when(categoryService.update(any(), anyLong())).thenReturn(response);

            // When & Then
            mockMvc.perform(put("/categories/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }
    }

    @Nested
    class DeleteCategory {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // When & Then
            mockMvc.perform(delete("/categories/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }
}