package com.teamfresh.example.infrastructure.product;

import com.teamfresh.example.domain.product.ProductStock;
import com.teamfresh.example.infrastructure.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(schema = "test", name = "product_stock")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private int stock;

    public ProductStock toDomain() {
        if (product == null) {
            return new ProductStock(id, null, stock, getCreatedAt(), getUpdatedAt());
        }
        return new ProductStock(id, product.toDomain(), stock, getCreatedAt(), getUpdatedAt());
    }

    public static ProductStockEntity toEntity(ProductStock productStock) {
        ProductStockEntity productStockEntity = new ProductStockEntity(
                productStock.getId(),
                ProductEntity.toEntity(productStock.getProduct()),
                productStock.getStock()
        );

        if (productStock.getId() == null) {
            productStockEntity.setCreatedAt(LocalDateTime.now());
        } else {
            productStockEntity.setCreatedAt(productStock.getCreatedAt());
        }

        productStockEntity.setUpdatedAt(LocalDateTime.now());

        return productStockEntity;
    }
}
