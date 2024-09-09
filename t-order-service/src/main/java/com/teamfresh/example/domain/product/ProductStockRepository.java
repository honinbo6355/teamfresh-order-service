package com.teamfresh.example.domain.product;

public interface ProductStockRepository {
    ProductStock findByProductIdWithLock(long productId);
    ProductStock save(ProductStock productStock);
}
