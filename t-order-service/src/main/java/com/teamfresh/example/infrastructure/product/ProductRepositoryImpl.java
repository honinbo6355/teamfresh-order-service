package com.teamfresh.example.infrastructure.product;

import com.teamfresh.example.domain.product.Product;
import com.teamfresh.example.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<Product> findById(long id) {
        return productJpaRepository.findById(id)
                .map(ProductEntity::toDomain);
    }
}
