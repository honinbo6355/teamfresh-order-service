package com.teamfresh.example.infrastructure.product;

import com.teamfresh.example.domain.product.Product;
import com.teamfresh.example.infrastructure.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(schema = "test", name = "product")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private long price;

    public Product toDomain() {
        return new Product(id, name, price, getCreatedAt(), getUpdatedAt());
    }

    public static ProductEntity toEntity(Product product) {
        ProductEntity productEntity = new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice()
        );

        if (product.getId() == null) {
            productEntity.setCreatedAt(LocalDateTime.now());
        } else {
            productEntity.setCreatedAt(product.getCreatedAt());
        }

        productEntity.setUpdatedAt(LocalDateTime.now());
        return productEntity;
    }
}
