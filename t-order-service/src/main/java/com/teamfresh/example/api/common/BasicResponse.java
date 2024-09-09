package com.teamfresh.example.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class BasicResponse<T> {
    private T result;
    private BasicError error;

    public BasicResponse(T result, BasicError basicError) {
        this.result = result;
        this.error = basicError;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicError {
        private String code;
        private String message;
    }
}
