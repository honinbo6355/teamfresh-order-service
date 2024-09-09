package com.teamfresh.example.api.orders;

import com.teamfresh.example.api.common.BasicResponse;
import com.teamfresh.example.api.orders.dto.OrderRequest;
import com.teamfresh.example.api.orders.dto.OrderResponse;
import com.teamfresh.example.domain.orders.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("checkout")
    public ResponseEntity<BasicResponse<OrderResponse>> checkout(@RequestBody OrderRequest orderRequest) {
        String orderNum = orderService.checkout(orderRequest);

        return new ResponseEntity<>(new BasicResponse<>(new OrderResponse(orderNum), null), HttpStatus.CREATED);
    }
}
