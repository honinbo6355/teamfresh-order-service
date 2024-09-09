package com.teamfresh.example.infrastructure.orders;

import com.teamfresh.example.domain.orders.OrderItem;
import com.teamfresh.example.infrastructure.BaseTimeEntity;
import com.teamfresh.example.infrastructure.product.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(schema = "test", name = "order_item")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private int quantity;
    private long orderPrice;

    public OrderItem toDomain() {
        return new OrderItem(id, product.toDomain(), order.toDomain(), quantity, orderPrice, getCreatedAt(), getUpdatedAt());
    }

    public static OrderItemEntity toEntity(OrderItem orderItem) {
        OrderItemEntity orderItemEntity = new OrderItemEntity(
                orderItem.getId(),
                ProductEntity.toEntity(orderItem.getProduct()),
                OrderEntity.toEntity(orderItem.getOrder()),
                orderItem.getQuantity(),
                orderItem.getOrderPrice()
        );

        if (orderItem.getId() == null) {
            orderItemEntity.setCreatedAt(LocalDateTime.now());
        } else {
            orderItemEntity.setCreatedAt(orderItem.getCreatedAt());
        }

        orderItemEntity.setUpdatedAt(LocalDateTime.now());

        return orderItemEntity;
    }
}
