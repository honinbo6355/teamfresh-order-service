package com.teamfresh.example.infrastructure.product;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStockJpaRepository extends JpaRepository<ProductStockEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select a from ProductStockEntity a where a.product.id = :productId")
    ProductStockEntity findByProductIdWithPessimisticLock(long productId);
}
