package com.marketplace.service.impl;

import com.marketplace.dto.LabelDto;
import com.marketplace.entity.Label;
import com.marketplace.exception.AlreadyExistsException;
import com.marketplace.exception.NotFoundException;
import com.marketplace.repository.LabelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("LabelService")
@ExtendWith(MockitoExtension.class)
public class LabelServiceImplTest
{
    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private LabelServiceImpl labelService;

    @Nested
    @DisplayName("Create Label")
    @Tag("Tests for createLabel method")
    class CreateLabelTest {

        @Test
        @DisplayName("Should create and save a valid label")
        void createLabel_ValidLabel_SavesLabel() {
            // Arrange
            LabelDto labelDto = new LabelDto();
            labelDto.setName("Name label");
            Label savedLabel = Label.builder()
                    .id(1L)
                    .name("Name label")
                    .description("Description label")
                    .build();

            when(labelRepository.existsByNameIgnoreCase(any())).thenReturn(false);
            when(labelRepository.save(any())).thenReturn(savedLabel);

            // Act
            LabelDto result = labelService.createLabel(labelDto);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(labelRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("Should throw AlreadyExistsException when label with this name already exists")
        void createLabel_AlreadyExists_ThrowsAlreadyExistsException() {
            // Arrange
            LabelDto request = new LabelDto(1L, "Name label", "Description label");
            when(labelRepository.existsByNameIgnoreCase(any())).thenReturn(true);

            // Act & Assert
            AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> labelService.createLabel(request));
            assertEquals("Le nom du label existe déjà", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when label name is null")
        void createLabel_InvalidRequest_NullName_ThrowsIllegalArgumentException() {
            // Arrange
            LabelDto request = new LabelDto(1L, null, "Description label");
            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> labelService.createLabel(request));
            assertEquals("Le nom du label est obligatoire et ne peut pas être nul", exception.getMessage());
        }
    }

    @Nested
    @Tag("Tests for updateLabel method")
    @DisplayName("Update Label")
    class UpdateLabelTest {
            @Test
            @DisplayName("Should update and return label")
            void updateLabel_ReturnsUpdatedLabel() {
                // Arrange
                Label existingLabel = Label.builder().id(1L).name("Name label").description("Description label").build();
                Label updatedLabel = Label.builder().id(1L).name("Updated Name").description("Updated Description").build();
                LabelDto labelDto = LabelDto.builder().name("Updated Name").description("Updated Description").build();

                when(labelRepository.findById(1L)).thenReturn(Optional.of(existingLabel));
                when(labelRepository.save(any(Label.class))).thenReturn(updatedLabel);

                // Act
                LabelDto result = labelService.updateLabel(labelDto, 1L);

                // Assert
                assertNotNull(result);
                assertEquals("Updated Name", result.getName());
                assertEquals("Updated Description", result.getDescription());
                verify(labelRepository, times(1)).findById(1L);
                verify(labelRepository, times(1)).save(any(Label.class));
            }

            @Test
            @DisplayName("Should throw NotFoundException when label not found")
            void updateLabel_NotFound_ThrowsNotFoundException() {
                // Arrange
                LabelDto labelDto = LabelDto.builder().name("Updated Name").description("Updated Description").build();
                when(labelRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                NotFoundException exception = assertThrows(NotFoundException.class, () -> labelService.updateLabel(labelDto, 1L));
                assertEquals("Il n'existe pas de label avec cet id", exception.getMessage());

            }
    }

    @Nested
    @DisplayName("Get All Labels")
    @Tag("Tests for getAllLabels method")
    class GetAllLabelsTest {

        @Test
        @DisplayName("Should return all labels")
        void getAllLabels_ReturnsAllLabels() {
            // Arrange
            Label label = Label.builder().id(1L).name("Name label").description("Description label").build();
            when(labelRepository.findAll()).thenReturn(List.of(label));

            // Act
            List<LabelDto> result = labelService.getAllLabels();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Name label", result.get(0).getName());

            verify(labelRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Get Label By Id")
    @Tag("Tests for getLabelById method")
    class GetLabelByIdTest {
            @Test
            @DisplayName("Should return label by id")
            void getLabelById_ReturnsLabel() {
                // Arrange
                Label label = Label.builder().id(1L).name("Name label").description("Description label").build();
                when(labelRepository.findById(1L)).thenReturn(Optional.of(label));
                // Act
                LabelDto result = labelService.getLabelById(1L);
                // Assert
                assertNotNull(result);
                assertEquals("Name label", result.getName());
            }

            @Test
            @DisplayName("Should throw NotFoundException when label not found")
            void getLabelById_NotFound_ThrowsNotFoundException() {
                // Arrange
                when(labelRepository.findById(1L)).thenReturn(Optional.empty());
                // Act & Assert
                NotFoundException exception = assertThrows(NotFoundException.class, () -> labelService.getLabelById(1L));
                assertEquals("Il n'existe pas de label avec cet id", exception.getMessage());
                verify(labelRepository, times(1)).findById(1L);
            }
    }

    @Nested
    @DisplayName("Delete Label")
    @Tag("Tests for deleteLabel method")
    class DeleteLabelTest {
        @Test
        @DisplayName("Should delete label")
        void deleteLabel_DeletesLabel() {
            // Arrange
            Label label = Label.builder().id(1L).name("Name label").description("Description label").build();
            when(labelRepository.findById(1L)).thenReturn(Optional.of(label));
            doNothing().when(labelRepository).deleteById(1L);
            // Act
            labelService.deleteLabel(1L);
            // Assert
            verify(labelRepository, times(1)).findById(1L);
            verify(labelRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw NotFoundException when label not found")
        void deleteLabel_NotFound_ThrowsNotFoundException() {
            // Arrange
            when(labelRepository.findById(1L)).thenReturn(Optional.empty());
            // Act & Assert
            NotFoundException exception = assertThrows(NotFoundException.class, () -> labelService.deleteLabel(1L));
            assertEquals("Il n'existe pas de label avec cet id", exception.getMessage());
            verify(labelRepository, never()).deleteById(1L);
            verify(labelRepository, times(1)).findById(1L);
        }
    }
}
