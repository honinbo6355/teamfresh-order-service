package com.teamfresh.example.api.orders.dto;

public record OrderRequestDto(
    long productId,
    String productName,
    int quantity
) {

}
