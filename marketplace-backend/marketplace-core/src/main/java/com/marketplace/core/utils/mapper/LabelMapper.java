package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.label.LabelDto;
import com.marketplace.core.entity.Label;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    LabelDto toDto(Label label);

    Label toEntity(LabelDto labelDto);

    List<LabelDto> toDtoList(List<Label> labels);
}
