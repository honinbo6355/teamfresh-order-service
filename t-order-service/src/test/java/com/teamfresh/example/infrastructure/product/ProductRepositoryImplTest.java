package com.teamfresh.example.infrastructure.product;

import com.teamfresh.example.domain.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({ProductRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryImplTest {

    @Autowired
    private ProductRepositoryImpl productRepositoryImpl;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Test
    @DisplayName("아이디로 상품 조회")
    public void find_by_id() {
        // given
        ProductEntity savedProductEntity = productJpaRepository.save(new ProductEntity(null, "에센셜 탱크 탑[다크 크레이]", 11200));

        // when
        Product findProduct = productRepositoryImpl.findById(savedProductEntity.getId())
                .orElseThrow(NullPointerException::new);
        // then
        Assertions.assertThat(findProduct.getName()).isEqualTo(savedProductEntity.getName());
        Assertions.assertThat(findProduct.getPrice()).isEqualTo(savedProductEntity.getPrice());
    }
}
