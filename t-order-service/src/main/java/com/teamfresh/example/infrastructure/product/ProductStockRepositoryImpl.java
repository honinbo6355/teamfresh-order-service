package com.teamfresh.example.infrastructure.product;

import com.teamfresh.example.domain.product.ProductStock;
import com.teamfresh.example.domain.product.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductStockRepositoryImpl implements ProductStockRepository {

    private final ProductStockJpaRepository productStockJpaRepository;

    @Override
    public ProductStock findByProductIdWithLock(long productId) {
        return productStockJpaRepository.findByProductIdWithPessimisticLock(productId)
                .toDomain();
    }

    @Override
    public ProductStock save(ProductStock productStock) {
        return productStockJpaRepository.save(ProductStockEntity.toEntity(productStock))
                .toDomain();
    }
}
