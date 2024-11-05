package com.marketplace.service.impl;

import com.marketplace.entity.Product;
import com.marketplace.repository.ProductRepository;
import com.marketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        if (product.getListeIngredients() != null) {
            String ingredientsAsString = String.join(", ", product.getListeIngredients().split(", "));
            product.setListeIngredients(ingredientsAsString);
        }

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setNom(productDetails.getNom());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPhoto(productDetails.getPhoto());
                    existingProduct.setPrixUnitaire(productDetails.getPrixUnitaire());
                    existingProduct.setValeurNutri(productDetails.getValeurNutri());
                    existingProduct.setListeIngredients(productDetails.getListeIngredients());
                    existingProduct.setQteStock(productDetails.getQteStock());
                    existingProduct.setSeuilCritique(productDetails.getSeuilCritique());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}


