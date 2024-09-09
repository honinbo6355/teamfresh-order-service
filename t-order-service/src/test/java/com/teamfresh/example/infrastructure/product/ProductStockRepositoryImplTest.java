package com.teamfresh.example.infrastructure.product;

import com.teamfresh.example.domain.product.ProductStock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({ProductStockRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductStockRepositoryImplTest {

    @Autowired
    private ProductStockRepositoryImpl productStockRepositoryImpl;

    @Autowired
    private ProductStockJpaRepository productStockJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Test
    @DisplayName("상품 아이디로 상품 재고 조회(비관적 락 적용)")
    public void find_by_product_id_with_lock() {
        // given
        ProductEntity savedProductEntity = productJpaRepository.save(new ProductEntity(null, "에센셜 탱크 탑[다크 크레이]", 11200));
        ProductStockEntity savedProductStockEntity = productStockJpaRepository.save(new ProductStockEntity(null, savedProductEntity, 20));

        // when
        ProductStock findProductStock = productStockRepositoryImpl.findByProductIdWithLock(savedProductEntity.getId());

        // then
        Assertions.assertThat(findProductStock.getStock()).isEqualTo(savedProductStockEntity.getStock());
    }

    @Test
    @DisplayName("상품 재고 등록")
    public void save() {
        // given
        ProductEntity savedProductEntity = productJpaRepository.save(new ProductEntity(null, "에센셜 탱크 탑[다크 크레이]", 11200));
        ProductStock productStock = new ProductStock(null, savedProductEntity.toDomain(), 20, null, null);

        // when
        ProductStock savedProductStock = productStockRepositoryImpl.save(productStock);

        // then
        Assertions.assertThat(savedProductStock.getStock()).isEqualTo(productStock.getStock());
    }
}
