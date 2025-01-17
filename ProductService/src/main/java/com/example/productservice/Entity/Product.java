package com.example.productservice.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @NotBlank
    @Column(name = "name", nullable = false)
    String name;
    @NotBlank
    @Column(name = "category", nullable = false)
    String category;
    @Column(name = "price", nullable = false)
    BigDecimal price;
    @NotBlank
    @Column(name = "quantity", nullable = false)
    Long quantity;
    @NotBlank
    @Column(name = "manufacturer", nullable = false)
    String manufacturer;
}
