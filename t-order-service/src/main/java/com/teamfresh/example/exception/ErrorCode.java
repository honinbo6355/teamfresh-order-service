package com.teamfresh.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    EMPTY_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "100", "상품명이 비어있습니다."),
    INVALID_PRODUCT_QUANTITY(HttpStatus.BAD_REQUEST, "101", "상품 주문 수량이 0 이하입니다."),
    EMPTY_USER_NAME(HttpStatus.BAD_REQUEST, "102", "주문자명이 비어있습니다."),
    EMPTY_ADDRESS(HttpStatus.BAD_REQUEST, "103", "주문자 주소가 비어있습니다."),
    EMPTY_ORDER_INFO(HttpStatus.BAD_REQUEST, "104", "주문 아이템 정보가 비어있습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "105", "상품이 존재하지 않습니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "106", "상품 재고가 부족합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "107", "내부 서버에 에러가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
