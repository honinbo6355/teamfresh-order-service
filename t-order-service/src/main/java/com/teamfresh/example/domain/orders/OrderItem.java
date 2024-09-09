package com.teamfresh.example.domain.orders;

import com.teamfresh.example.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderItem {
    private Long id;
    private Product product;
    private Orders order;
    private int quantity;
    private long orderPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        calculateOrderPrice();
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    private void calculateOrderPrice() {
        this.orderPrice = product.getPrice() * quantity;
    }
}
