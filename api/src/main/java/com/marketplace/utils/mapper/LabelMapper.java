package com.marketplace.utils.mapper;

import com.marketplace.dto.LabelDto;
import com.marketplace.entity.Label;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    LabelMapper INSTANCE = Mappers.getMapper( LabelMapper.class );

    LabelDto toDto(Label label);

    Label toEntity(LabelDto labelDto);

    List<LabelDto> toDtoList(List<Label> labels);
}
