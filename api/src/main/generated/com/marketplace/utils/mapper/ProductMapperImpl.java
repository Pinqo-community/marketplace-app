package com.marketplace.utils.mapper;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
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
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto.ProductDtoBuilder productDto = ProductDto.builder();

        productDto.id( product.getId() );
        productDto.name( product.getName() );
        productDto.description( product.getDescription() );
        productDto.photo( product.getPhoto() );
        productDto.unitPrice( product.getUnitPrice() );
        productDto.nutritionalValue( product.getNutritionalValue() );
        productDto.listOfIngredients( product.getListOfIngredients() );
        productDto.stockQuantity( product.getStockQuantity() );
        productDto.maxQuantityByPurchase( product.getMaxQuantityByPurchase() );
        productDto.stepQuantity( product.getStepQuantity() );
        productDto.criticalLevel( product.getCriticalLevel() );
        productDto.active( product.getActive() );

        return productDto.build();
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( productDto.getId() );
        product.name( productDto.getName() );
        product.description( productDto.getDescription() );
        product.photo( productDto.getPhoto() );
        product.unitPrice( productDto.getUnitPrice() );
        product.nutritionalValue( productDto.getNutritionalValue() );
        product.listOfIngredients( productDto.getListOfIngredients() );
        product.stockQuantity( productDto.getStockQuantity() );
        product.maxQuantityByPurchase( productDto.getMaxQuantityByPurchase() );
        product.stepQuantity( productDto.getStepQuantity() );
        product.criticalLevel( productDto.getCriticalLevel() );
        product.active( productDto.getActive() );

        return product.build();
    }

    @Override
    public List<ProductDto> toDtoList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductDto> list = new ArrayList<ProductDto>( products.size() );
        for ( Product product : products ) {
            list.add( toDto( product ) );
        }

        return list;
    }
}
