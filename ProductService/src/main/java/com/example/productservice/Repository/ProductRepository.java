package com.example.productservice.Repository;

import com.example.productservice.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product getProductByName(String name);

    Product getProductById(Long id);

    List<Product> getProductsByCategory(String category);

    Integer countProductById(Long id);

    Product save(Product product);

    @Modifying
    @Query("update Product p set p.name=?2,p.category=?3,p.price=?4,p.quantity=?5,p.manufacturer=?6 where p.id=?1")
    Product updateProduct(Long id, String name, String category, BigDecimal price, Long quantity, String manufacturer);

    @Modifying
    Product deleteProductById(Long id);
}
