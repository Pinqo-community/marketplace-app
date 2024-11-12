//@Test
///**
// * Vérifie que ProductManager convertit correctement un ProductDTO en Product avec toutes les valeurs renseignées
// */
//public void ProductMapperTest() {
//    ProductDto productDto = ProductDto.builder()
//            .name("Produit Test")
//            .description("Description Test")
//            .unitPrice(BigDecimal.valueOf(100))
//            .stockQuantity(10)
//            .criticalLevel(2)
//            .build();
//
//    Product product = ProductMapper.INSTANCE.toEntity(productDto);
//
//    assertEquals(productDto.getName(), product.getName());
//    assertEquals(productDto.getDescription(), product.getDescription());
//    assertEquals(productDto.getUnitPrice(), product.getUnitPrice());
//    assertEquals(productDto.getStockQuantity(), product.getStockQuantity());
//    assertEquals(productDto.getCriticalLevel(), product.getCriticalLevel());
//}
