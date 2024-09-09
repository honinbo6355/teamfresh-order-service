package com.teamfresh.example.api.orders.dto;

import com.teamfresh.example.exception.CustomException;
import com.teamfresh.example.exception.ErrorCode;

import java.util.List;

public record OrderRequest(
    String userName,
    String address,
    List<OrderRequestDto> orderInfos
) {
    public void validate() {
        if (userName == null || userName.isBlank()) {
            throw new CustomException(ErrorCode.EMPTY_USER_NAME);
        }
        if (address == null || address.isBlank()) {
            throw new CustomException(ErrorCode.EMPTY_ADDRESS);
        }

        if (orderInfos == null || orderInfos.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_ORDER_INFO);
        }

        for (OrderRequestDto orderInfo : orderInfos) {
            if (orderInfo.productName() == null || orderInfo.productName().isBlank()) {
                throw new CustomException(ErrorCode.EMPTY_PRODUCT_NAME);
            }
            if (orderInfo.quantity() <= 0) {
                throw new CustomException(ErrorCode.INVALID_PRODUCT_QUANTITY);
            }
        }
    }
}
