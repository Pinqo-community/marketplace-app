package com.marketplace.utils.mapper;

import com.marketplace.dto.category.CategoryResponseDto;
import com.marketplace.entity.Category;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-19T00:24:48+0100",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponseDto toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        Long id = null;
        String name = null;

        id = category.getId();
        name = category.getName();

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto( id, name );

        return categoryResponseDto;
    }

    @Override
    public List<CategoryResponseDto> toResponseList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryResponseDto> list = new ArrayList<CategoryResponseDto>( categories.size() );
        for ( Category category : categories ) {
            list.add( toResponse( category ) );
        }

        return list;
    }
}
