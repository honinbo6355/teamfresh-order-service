package com.teamfresh.example.infrastructure.orders;

import com.teamfresh.example.domain.orders.OrderItem;
import com.teamfresh.example.infrastructure.product.ProductEntity;
import com.teamfresh.example.infrastructure.product.ProductJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({OrderItemRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderItemRepositoryImplTest {

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private OrderItemRepositoryImpl orderItemRepositoryImpl;

    @Test
    @DisplayName("주문 아이템 등록")
    public void save() {
        // given
        OrderEntity savedOrderEntity = orderJpaRepository.save(new OrderEntity(null, "240909-18205303", 33600L, "김프레시", "서울특별시 송파구 송파대로 111 201동 803호"));
        ProductEntity savedProductEntity = productJpaRepository.save(new ProductEntity(null, "에센셜 탱크 탑[다크 크레이]", 11200));
        OrderItem orderItem = new OrderItem(null, savedProductEntity.toDomain(), savedOrderEntity.toDomain(), 3, 33600L, null, null);

        // when
        OrderItem savedOrderItem = orderItemRepositoryImpl.save(orderItem);

        // then
        Assertions.assertThat(savedOrderItem.getQuantity()).isEqualTo(orderItem.getQuantity());
        Assertions.assertThat(savedOrderItem.getOrderPrice()).isEqualTo(orderItem.getOrderPrice());
    }
}
