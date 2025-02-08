package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.label.LabelDto;
import com.marketplace.core.entity.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

class LabelMapperTest {
    private LabelMapper labelMapper;

    @BeforeEach
    void setUp() {
        labelMapper = Mappers.getMapper(LabelMapper.class);
    }

    @Test
    void toDto_whenValidLabel_thenMapAllFields() {
        Label label = Label.builder()
                .id(1L)
                .name("Test Label")
                .description("Test Description")
                .build();

        LabelDto result = labelMapper.toDto(label);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(label.getId());
        assertThat(result.getName()).isEqualTo(label.getName());
        assertThat(result.getDescription()).isEqualTo(label.getDescription());
    }

    @Test
    void toDto_whenNullLabel_thenReturnNull() {
        assertThat(labelMapper.toDto(null)).isNull();
    }

    @Test
    void toEntity_whenValidDto_thenMapAllFields() {
        LabelDto dto = new LabelDto();
        dto.setId(1L);
        dto.setName("Test Label");
        dto.setDescription("Test Description");

        Label result = labelMapper.toEntity(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    void toEntity_whenNullDto_thenReturnNull() {
        assertThat(labelMapper.toEntity(null)).isNull();
    }

    @Test
    void toDtoList_whenValidLabelList_thenMapAllLabels() {
        List<Label> labels = Arrays.asList(
                Label.builder().id(1L).name("Label 1").build(),
                Label.builder().id(2L).name("Label 2").build()
        );

        List<LabelDto> result = labelMapper.toDtoList(labels);

        assertThat(result)
                .hasSize(2)
                .extracting(LabelDto::getId, LabelDto::getName)
                .containsExactly(
                        tuple(1L, "Label 1"),
                        tuple(2L, "Label 2")
                );
    }

    @Test
    void toDtoList_whenNullList_thenReturnNull() {
        assertThat(labelMapper.toDtoList(null)).isNull();
    }

    @Test
    void toDtoList_whenEmptyList_thenReturnEmptyList() {
        assertThat(labelMapper.toDtoList(List.of())).isEmpty();
    }
}