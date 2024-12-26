package com.marketplace.controller;

import com.marketplace.annotation.ControllerWebMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.dto.LabelDto;
import com.marketplace.exception.AlreadyExistsException;
import com.marketplace.exception.NotFoundException;
import com.marketplace.service.LabelService;
import com.marketplace.utils.mapper.LabelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ControllerWebMvcTest(LabelController.class)
@DisplayName("Tests for LabelController")
class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LabelService labelService;

    @Autowired
    private ObjectMapper objectMapper;
    private LabelDto baseLabelDto;

    @BeforeEach
    public void setup() {
        baseLabelDto = new LabelDto(
                1L,
                "Name label",
                "Description label"
        );
    }

    @Nested
    class GetAllLabels {
        @Test
        @DisplayName("Should return all labels")
        void shouldReturnAllLabels() throws Exception {
            //Arrange
            List<LabelDto> labelDtos = List.of(baseLabelDto);

            Mockito.when(labelService.getAllLabels()).thenReturn(labelDtos);

            // Act & Assert
            mockMvc.perform(get("/labels")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].name").value("Name label"));
        }
    }

    @Nested
    class GetLabelById {
        @Test
        @DisplayName("Should return label by ID")
        void shouldReturnLabelById() throws Exception {
            //Arrange
            Mockito.when(labelService.getLabelById(anyLong())).thenReturn(baseLabelDto);

            // Act & Assert
            mockMvc.perform(get("/labels/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Name label"));
        }

        @Test
        @DisplayName("Should return 404 when label not found")
        void shouldReturnNotFoundForLabelNotFound() throws Exception {
            // Arrange
            Mockito.when(labelService.getLabelById(anyLong())).thenThrow(new NotFoundException("Il n'existe pas de label avec cet id"));

            // Act & Assert
            mockMvc.perform(get("/labels/{id}", 999L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Il n'existe pas de label avec cet id"));
        }

    }

    @Nested
    class CreateLabel {
        @Test
        @DisplayName("Should create a label")
        void shouldCreateLabel() throws Exception {
            //Arrange
           Mockito.when(labelService.createLabel(any(LabelDto.class))).thenReturn(baseLabelDto);

            // Act & Assert
            mockMvc.perform(post("/labels")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(baseLabelDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Name label"));
        }

        @Test
        @DisplayName("Should return 409 when label with this name already exists")
        void shouldReturnBadRequestForAlreadyExists() throws Exception {
            //Arrange
            Mockito.when(labelService.createLabel(any(LabelDto.class))).thenThrow(new AlreadyExistsException("Le nom du label existe déjà"));
            // Act & Assert
            mockMvc.perform(post("/labels")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(baseLabelDto)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("Le nom du label existe déjà"));
        }
    }

    @Nested
    class UpdateLabel {

        @Test
        @DisplayName("Should update label")
        void shouldUpdateLabel() throws Exception {
            //Arrange
            LabelDto updatedDto = new LabelDto(1L, "Updated Name", "Updated Description");
            Mockito.when(labelService.updateLabel(any(LabelDto.class), anyLong())).thenReturn(updatedDto);

            // Act & Assert
            mockMvc.perform(put("/labels/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated Name"))
                    .andExpect(jsonPath("$.description").value("Updated Description"));
        }
    }

    @Nested
    class DeleteLabel {
        @Test
        @DisplayName("Should delete label")
        void shouldDeleteLabel() throws Exception {
            //Arrange
            Mockito.doNothing().when(labelService).deleteLabel(anyLong());

            // Act & Assert
            mockMvc.perform(delete("/labels/{id}", 1L))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should return 404 when label not found")
        void shouldReturnNotFoundForDelete() throws Exception {
            //Arrange
            Mockito.doThrow(new NotFoundException("Label not found")).when(labelService).deleteLabel(anyLong());
            // Act & Assert
            mockMvc.perform(delete("/labels/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Label not found"));
        }

    }
}
