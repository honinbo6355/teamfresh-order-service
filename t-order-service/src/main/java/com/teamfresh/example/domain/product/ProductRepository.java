package com.teamfresh.example.domain.product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(long id);
}
