package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.label.LabelDto;
import com.marketplace.core.entity.Label;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper interface for converting between Label entities and DTOs.
 * Uses MapStruct for automatic mapping implementation.
 */
@Mapper(componentModel = "spring")
public interface LabelMapper {
    /**
     * Converts a Label entity to a DTO.
     *
     * @param label the label entity to convert
     * @return the converted label DTO
     */
    LabelDto toDto(Label label);

    /**
     * Converts a Label DTO to an entity.
     *
     * @param labelDto the label DTO to convert
     * @return the converted label entity
     */
    Label toEntity(LabelDto labelDto);

    /**
     * Converts a list of Label entities to DTOs.
     *
     * @param labels list of label entities to convert
     * @return list of converted label DTOs
     */
    List<LabelDto> toDtoList(List<Label> labels);
}
