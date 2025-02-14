package com.marketplace.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.api.dto.product.ProductDto;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.api.service.ProductService;
import com.marketplace.web.configuration.WebMvcBaseTest;
import com.marketplace.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = ProductController.class)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest  extends WebMvcBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private ProductDto baseProductDto;

    @BeforeEach
    void setup() {
        baseProductDto = new ProductDto(1L, "Miel de lavande", "Pot de 300g", "img_url",
                new BigDecimal("10.00"), "High", "", 30, 50, 1, 2, true);
    }

    @Nested
    class CreateProduct {
        @Test
        void whenValidProduct_thenCreate() throws Exception {
            when(productService.createProduct(any(ProductDto.class))).thenReturn(baseProductDto);

            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(baseProductDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Miel de lavande"));
        }

        @Test
        void whenInvalidProduct_thenBadRequest() throws Exception {
            ProductDto invalidDto = new ProductDto(null, null, "desc", null, null,
                    "Low", "", 0, 0, 0, 0, false);

            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetAvailableProducts {
        @Test
        void whenGetAvailable_thenReturnProducts() throws Exception {
            List<ProductDto> products = List.of(baseProductDto);
            when(productService.getAvailableProducts()).thenReturn(products);

            mockMvc.perform(get("/products/available"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].name").value("Miel de lavande"));
        }
    }

    @Nested
    class getHomePageProducts {
        @Test
        void whenGetHomePage_thenReturn8Products() throws Exception {
            List<ProductDto> products = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                products.add(new ProductDto(
                        (long) i,
                        "Product " + i,
                        "Description " + i,
                        "img_url_" + i,
                        BigDecimal.valueOf(10.00 + i),
                        "High",
                        "Category",
                        30,
                        50,
                        1,
                        2,
                        true
                ));
            }
            when(productService.getAvailableProductsForHomePage()).thenReturn(products);

            mockMvc.perform(get("/products/homePage"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(8))
                    .andExpect(jsonPath("$[0].id").value(1L));
        }
    }

    @Nested
    class GetProductById {
        @Test
        void whenValidId_thenReturnProduct() throws Exception {
            when(productService.getProductById(1L)).thenReturn(baseProductDto);

            mockMvc.perform(get("/products/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Miel de lavande"));
        }

        @Test
        void whenInvalidId_thenNotFound() throws Exception {
            when(productService.getProductById(1L))
                    .thenThrow(new NotFoundException("Product not found"));

            mockMvc.perform(get("/products/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Product not found"));
        }
    }

    @Nested
    class UpdateProduct {
        @Test
        void whenValidUpdate_thenSuccess() throws Exception {
            ProductDto updatedDto = new ProductDto(1L, "Updated Miel", "Pot 500g", "new_img",
                    new BigDecimal("15.00"), "High", "", 50, 100, 1, 2, true);
            when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(updatedDto);

            mockMvc.perform(put("/products/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated Miel"));
        }
    }

    @Nested
    class GetProductsByStatus {
        @Test
        void whenValidStatus_thenReturnProducts() throws Exception {
            when(productService.getProductsByStatus(true)).thenReturn(List.of(baseProductDto));

            mockMvc.perform(get("/products/status")
                            .param("active", "true"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1L));
        }

        @Test
        void whenMissingStatus_thenBadRequest() throws Exception {
            mockMvc.perform(get("/products/status"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void whenEmptyStatus_thenBadRequest() throws Exception {
            mockMvc.perform(get("/products/status")
                            .param("active", ""))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void whenInvalidStatus_thenBadRequest() throws Exception {
            mockMvc.perform(get("/products/status")
                            .param("active", "invalid"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteProduct {
        @Test
        void whenValidId_thenDelete() throws Exception {
            doNothing().when(productService).deleteProduct(1L);

            mockMvc.perform(delete("/products/{id}", 1L))
                    .andExpect(status().isNoContent());
        }

        @Test
        void whenInvalidId_thenNotFound() throws Exception {
            doThrow(new NotFoundException("Product not found"))
                    .when(productService).deleteProduct(1L);

            mockMvc.perform(delete("/products/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Product not found"));
        }
    }
}