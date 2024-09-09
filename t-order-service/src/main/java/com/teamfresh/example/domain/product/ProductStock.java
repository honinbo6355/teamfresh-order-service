package com.teamfresh.example.domain.product;

import com.teamfresh.example.exception.CustomException;
import com.teamfresh.example.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductStock {
    private Long id;
    private Product product;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void decreaseStock(int stock) {
        if (this.stock < stock) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }
        this.stock -= stock;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
