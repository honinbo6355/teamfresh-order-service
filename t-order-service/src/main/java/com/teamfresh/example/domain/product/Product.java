package com.teamfresh.example.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private long price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
