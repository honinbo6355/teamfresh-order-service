package com.teamfresh.example.infrastructure.orders;

import com.teamfresh.example.domain.orders.Orders;
import com.teamfresh.example.infrastructure.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(schema = "test", name = "orders")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNum;
    private long orderPrice;
    private String userName;
    private String address;

    public Orders toDomain() {
        return new Orders(id, orderPrice, userName, address, orderNum, getCreatedAt(), getUpdatedAt());
    }

    public static OrderEntity toEntity(Orders order) {
        OrderEntity orderEntity = new OrderEntity(
                order.getId(),
                order.getOrderNum(),
                order.getOrderPrice(),
                order.getUserName(),
                order.getAddress()
        );

        if (order.getId() == null) {
            orderEntity.setCreatedAt(LocalDateTime.now());
        } else {
            orderEntity.setCreatedAt(order.getCreatedAt());
        }

        orderEntity.setUpdatedAt(LocalDateTime.now());
        return orderEntity;
    }
}
