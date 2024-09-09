package com.teamfresh.example.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamfresh.example.api.orders.dto.OrderRequest;
import com.teamfresh.example.api.orders.dto.OrderRequestDto;
import com.teamfresh.example.domain.orders.OrderService;
import com.teamfresh.example.infrastructure.orders.OrderEntity;
import com.teamfresh.example.infrastructure.orders.OrderItemEntity;
import com.teamfresh.example.infrastructure.orders.OrderItemJpaRepository;
import com.teamfresh.example.infrastructure.orders.OrderJpaRepository;
import com.teamfresh.example.infrastructure.product.ProductStockEntity;
import com.teamfresh.example.infrastructure.product.ProductStockJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductStockJpaRepository productStockJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private OrderItemJpaRepository orderItemJpaRepository;

    @Order(1)
    @Test
    @DisplayName("주문이 성공할 경우")
    public void checkout() throws Exception {
        // given
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, "시티 오브 더 라이트 볼캡 [블랙]", 3),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when & then
        mockMvc.perform(post("/api/order/checkout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.orderNum").isNotEmpty());
    }

    @Order(2)
    @Test
    @DisplayName("재고 12개인 상품을 유저 50명이 동시에 주문 테스트" +
            "유저당 상품 수량 1개씩 주문" +
            "정확히 12명만 주문되는지 확인")
    public void checkout_when_concurrency() throws InterruptedException {
        // given
        int userCount = 50;
        CountDownLatch latch = new CountDownLatch(userCount);
        ExecutorService executorService = Executors.newFixedThreadPool(userCount);
        long productId = 6L;

        // when
        for (int i=1; i<=userCount; i++) {
            String userName = "유저" + i;
            String address = "서울특별시 송파구 송파대로 111 201동 80" + i + "호";

            List<OrderRequestDto> orderInfos = Arrays.asList(
                    new OrderRequestDto(productId, "시티 오브 더 라이트 볼캡 [블랙]", 1)
            );
            OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

            executorService.submit(() -> {
                try {
                    orderService.checkout(orderRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        executorService.shutdown();
        latch.await();

        // then
        List<OrderEntity> orderEntities = orderJpaRepository.findAll();
        List<OrderItemEntity> orderItemEntities = orderItemJpaRepository.findAll();
        ProductStockEntity productStockEntity = productStockJpaRepository.findByProductId(productId);

        Assertions.assertThat(0).isEqualTo(productStockEntity.getStock());
        Assertions.assertThat(12).isEqualTo(orderEntities.size());
        Assertions.assertThat(12).isEqualTo(orderItemEntities.size());
    }
}
