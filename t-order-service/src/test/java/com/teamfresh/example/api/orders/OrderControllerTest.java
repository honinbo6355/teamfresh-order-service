package com.teamfresh.example.api.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamfresh.example.api.orders.dto.OrderRequest;
import com.teamfresh.example.api.orders.dto.OrderRequestDto;
import com.teamfresh.example.domain.orders.OrderService;
import com.teamfresh.example.exception.CustomException;
import com.teamfresh.example.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문이 성공할 경우")
    public void checkout() throws Exception {
        // given
        String orderNum = "240909-18205303";
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, "시티 오브 더 라이트 볼캡 [블랙]", 3),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when
        given(orderService.checkout(any(OrderRequest.class))).willReturn(orderNum);

        // then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.orderNum").value(orderNum));
    }

    @Test
    @DisplayName("주문시 주문자명이 비어있을경우")
    public void checkout_when_empty_user_name() throws Exception {
        // given
        String userName = null;
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, "시티 오브 더 라이트 볼캡 [블랙]", 3),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when
        willThrow(new CustomException(ErrorCode.EMPTY_USER_NAME)).given(orderService).checkout(any(OrderRequest.class));

        // then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.EMPTY_USER_NAME.getCode()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.EMPTY_USER_NAME.getMessage()));
    }

    @Test
    @DisplayName("주문시 주문자 주소가 비어있을경우")
    public void checkout_when_empty_address() throws Exception {
        // given
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        OrderRequest orderRequest = new OrderRequest(userName, address, null);

        // when
        willThrow(new CustomException(ErrorCode.EMPTY_ORDER_INFO)).given(orderService).checkout(any(OrderRequest.class));

        // then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.EMPTY_ORDER_INFO.getCode()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.EMPTY_ORDER_INFO.getMessage()));
    }

    @Test
    @DisplayName("주문시 주문 아이템 정보가 비어있을경우")
    public void checkout_when_empty_order_info() throws Exception {
        // given
        String userName = "김프레시";
        String address = null;

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, "시티 오브 더 라이트 볼캡 [블랙]", 3),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when
        willThrow(new CustomException(ErrorCode.EMPTY_ADDRESS)).given(orderService).checkout(any(OrderRequest.class));

        // then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.EMPTY_ADDRESS.getCode()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.EMPTY_ADDRESS.getMessage()));
    }

    @Test
    @DisplayName("주문시 상품명이 비어있을경우")
    public void checkout_when_empty_product_name() throws Exception {
        // given
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, null, 3),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when
        willThrow(new CustomException(ErrorCode.EMPTY_PRODUCT_NAME)).given(orderService).checkout(any(OrderRequest.class));

        // then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.EMPTY_PRODUCT_NAME.getCode()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.EMPTY_PRODUCT_NAME.getMessage()));
    }

    @Test
    @DisplayName("주문시 상품 주문 수량이 0 이하일경우")
    public void checkout_when_invalid_product_quantity() throws Exception {
        // given
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, "시티 오브 더 라이트 볼캡 [블랙]", -5),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when
        willThrow(new CustomException(ErrorCode.INVALID_PRODUCT_QUANTITY)).given(orderService).checkout(any(OrderRequest.class));

        // then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_PRODUCT_QUANTITY.getCode()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.INVALID_PRODUCT_QUANTITY.getMessage()));
    }

    @Test
    @DisplayName("주문시 상품이 존재하지 않을 경우")
    public void checkout_when_not_found_product() throws Exception {
        // given
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, "시티 오브 더 라이트 볼캡 [블랙]", 3),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when
        willThrow(new CustomException(ErrorCode.NOT_FOUND_PRODUCT)).given(orderService).checkout(any(OrderRequest.class));

        // then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.NOT_FOUND_PRODUCT.getCode()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.NOT_FOUND_PRODUCT.getMessage()));
    }

    @Test
    @DisplayName("주문시 상품 재고가 부족한 경우")
    public void checkout_when_out_of_stock_product() throws Exception {
        // given
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, "시티 오브 더 라이트 볼캡 [블랙]", 40),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 70)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when
        willThrow(new CustomException(ErrorCode.OUT_OF_STOCK)).given(orderService).checkout(any(OrderRequest.class));

        // then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.OUT_OF_STOCK.getCode()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.OUT_OF_STOCK.getMessage()));
    }

    @Test
    @DisplayName("주문시 예측하지 못한 런타임 서버 오류가 발생할경우")
    public void checkout_when_not_expect_internal_server_error() throws Exception {
        // given
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, "시티 오브 더 라이트 볼캡 [블랙]", 3),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when
        willThrow(new RuntimeException()).given(orderService).checkout(any(OrderRequest.class));

        // then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.INTERNAL_SERVER_ERROR.getCode()))
                .andExpect(jsonPath("$.error.message").value(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
