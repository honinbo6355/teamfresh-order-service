package com.teamfresh.example.infrastructure.orders;

import com.teamfresh.example.domain.orders.OrderRepository;
import com.teamfresh.example.domain.orders.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Orders save(Orders order) {
        return orderJpaRepository.save(OrderEntity.toEntity(order))
                .toDomain();
    }
}
