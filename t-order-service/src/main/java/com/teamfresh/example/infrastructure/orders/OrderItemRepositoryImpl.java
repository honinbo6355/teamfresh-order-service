package com.teamfresh.example.infrastructure.orders;

import com.teamfresh.example.domain.orders.OrderItem;
import com.teamfresh.example.domain.orders.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemJpaRepository.save(OrderItemEntity.toEntity(orderItem))
                .toDomain();
    }
}
