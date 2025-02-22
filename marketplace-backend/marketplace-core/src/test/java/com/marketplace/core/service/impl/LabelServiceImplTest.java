package com.marketplace.core.service.impl;

import com.marketplace.api.dto.label.LabelDto;
import com.marketplace.api.exception.AlreadyExistsException;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.core.entity.Label;
import com.marketplace.core.repository.LabelRepository;
import com.marketplace.core.utils.mapper.LabelMapper;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LabelServiceImplTest {
    @Mock
    private LabelRepository labelRepository;

    @Mock
    private LabelMapper labelMapper;

    @InjectMocks
    private LabelServiceImpl labelService;

    @Nested
    @DisplayName("Create Label")
    class CreateLabel {
        @Test
        void whenValidLabel_thenCreateAndReturnDto() {
            LabelDto inputDto = new LabelDto();
            inputDto.setName("Test Label");

            Label entity = new Label();
            Label savedEntity = new Label();
            LabelDto outputDto = new LabelDto();

            when(labelRepository.existsByNameIgnoreCase(inputDto.getName())).thenReturn(false);
            when(labelMapper.toEntity(inputDto)).thenReturn(entity);
            when(labelRepository.save(entity)).thenReturn(savedEntity);
            when(labelMapper.toDto(savedEntity)).thenReturn(outputDto);

            LabelDto result = labelService.createLabel(inputDto);

            assertThat(result).isEqualTo(outputDto);
            verify(labelRepository).existsByNameIgnoreCase(inputDto.getName());
            verify(labelMapper).toEntity(inputDto);
            verify(labelRepository).save(entity);
            verify(labelMapper).toDto(savedEntity);
        }

        @Test
        void whenNameExists_thenThrowException() {
            LabelDto inputDto = new LabelDto();
            inputDto.setName("Existing Label");

            when(labelRepository.existsByNameIgnoreCase(inputDto.getName())).thenReturn(true);

            assertThatThrownBy(() -> labelService.createLabel(inputDto))
                    .isInstanceOf(AlreadyExistsException.class)
                    .hasMessage("Le nom du label existe déjà");
        }
    }

    @Nested
    @DisplayName("Update Label")
    class UpdateLabel {
        @Test
        void whenValidUpdate_thenReturnDto() {
            Long id = 1L;
            LabelDto inputDto = new LabelDto();
            inputDto.setName("Updated Name");
            inputDto.setDescription("Updated Description");

            Label existingLabel = new Label();
            Label savedLabel = new Label();
            LabelDto outputDto = new LabelDto();

            when(labelRepository.findById(id)).thenReturn(Optional.of(existingLabel));
            when(labelRepository.save(existingLabel)).thenReturn(savedLabel);
            when(labelMapper.toDto(savedLabel)).thenReturn(outputDto);

            LabelDto result = labelService.updateLabel(inputDto, id);

            assertThat(result).isEqualTo(outputDto);
            verify(labelRepository).findById(id);
            verify(labelRepository).save(existingLabel);
            verify(labelMapper).toDto(savedLabel);
        }

        @Test
        void whenInvalidId_thenThrowException() {
            when(labelRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labelService.updateLabel(new LabelDto(), 1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Il n'existe pas de label avec cet id");
        }
    }

    @Nested
    @DisplayName("Get All Labels")
    class GetAllLabels {
        @Test
        void whenGetAll_thenReturnDtoList() {
            List<Label> labels = List.of(new Label());
            List<LabelDto> dtos = List.of(new LabelDto());

            when(labelRepository.findAll()).thenReturn(labels);
            when(labelMapper.toDtoList(labels)).thenReturn(dtos);

            List<LabelDto> result = labelService.getAllLabels();

            assertThat(result).isEqualTo(dtos);
            verify(labelRepository).findAll();
            verify(labelMapper).toDtoList(labels);
        }
    }

    @Nested
    @DisplayName("Get Label By Id")
    class GetLabelById {
        @Test
        void whenValidId_thenReturnDto() {
            Long id = 1L;
            Label label = new Label();
            LabelDto dto = new LabelDto();

            when(labelRepository.findById(id)).thenReturn(Optional.of(label));
            when(labelMapper.toDto(label)).thenReturn(dto);

            LabelDto result = labelService.getLabelById(id);

            assertThat(result).isEqualTo(dto);
            verify(labelRepository).findById(id);
            verify(labelMapper).toDto(label);
        }

        @Test
        void whenInvalidId_thenThrowException() {
            when(labelRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labelService.getLabelById(1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Il n'existe pas de label avec cet id");
        }
    }

    @Nested
    @DisplayName("Delete Label")
    class DeleteLabel {
        @Test
        void whenValidId_thenDelete() {
            Long id = 1L;
            when(labelRepository.findById(id)).thenReturn(Optional.of(new Label()));

            labelService.deleteLabel(id);

            verify(labelRepository).findById(id);
            verify(labelRepository).deleteById(id);
        }

        @Test
        void whenInvalidId_thenThrowException() {
            Long id = 1L;
            when(labelRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labelService.deleteLabel(id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Il n'existe pas de label avec cet id");

            verify(labelRepository, never()).deleteById(id);
        }
    }
}