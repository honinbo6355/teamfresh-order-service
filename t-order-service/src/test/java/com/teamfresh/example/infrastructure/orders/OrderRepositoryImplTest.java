package com.teamfresh.example.infrastructure.orders;

import com.teamfresh.example.domain.orders.Orders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({OrderRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryImplTest {

    @Autowired
    private OrderRepositoryImpl orderRepositoryImpl;

    @Test
    @DisplayName("주문 등록")
    public void save() {
        // given
        Orders order = new Orders(null, 50100L, "김프레시", "서울특별시 송파구 송파대로 111 201동 803호", "240909-18205303", null, null);

        // when
        Orders savedOrder = orderRepositoryImpl.save(order);

        // then
        Assertions.assertThat(savedOrder.getOrderPrice()).isEqualTo(order.getOrderPrice());
        Assertions.assertThat(savedOrder.getUserName()).isEqualTo(order.getUserName());
        Assertions.assertThat(savedOrder.getAddress()).isEqualTo(order.getAddress());
        Assertions.assertThat(savedOrder.getOrderNum()).isEqualTo(order.getOrderNum());
    }
}
