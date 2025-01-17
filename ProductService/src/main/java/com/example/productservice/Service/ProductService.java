package com.example.productservice.Service;

import com.example.productservice.Entity.Product;
import com.example.productservice.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(Long id) {
        return productRepository.getProductById(id);
    }

    public Product getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.getProductsByCategory(category);
    }

    public Integer countProductsById(Long id) {
        return productRepository.countProductById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        return productRepository.updateProduct(product.getId(), product.getName(), product.getCategory(), product.getPrice(), product.getQuantity(), product.getManufacturer());
    }

    public Product deleteProductById(Long id) {
        return productRepository.deleteProductById(id);
    }
}
