package com.teamfresh.example.domain.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class Orders {
    private Long id;
    private long orderPrice;
    private String userName;
    private String address;
    private String orderNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Orders(String userName, String address) {
        this.userName = userName;
        this.address = address;
        this.generateOrderNum();
    }

    public void addItem(OrderItem orderItem) {
        calculateOrderPrice(orderItem);
    }

    private void generateOrderNum() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd-HHmmssSS");
        this.orderNum = now.format(formatter);
    }
    
    private void calculateOrderPrice(OrderItem orderItem) {
        orderPrice += orderItem.getOrderPrice();
    }
}
