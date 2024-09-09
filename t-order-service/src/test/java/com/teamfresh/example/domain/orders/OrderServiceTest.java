package com.teamfresh.example.domain.orders;

import com.teamfresh.example.api.orders.dto.OrderRequest;
import com.teamfresh.example.api.orders.dto.OrderRequestDto;
import com.teamfresh.example.domain.product.Product;
import com.teamfresh.example.domain.product.ProductRepository;
import com.teamfresh.example.domain.product.ProductStock;
import com.teamfresh.example.domain.product.ProductStockRepository;
import com.teamfresh.example.exception.CustomException;
import com.teamfresh.example.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductStockRepository productStockRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("주문이 성공할 경우")
    public void checkout() throws Exception {
        // given
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        Product product = new Product(6L, "시티 오브 더 라이트 볼캡 [블랙]", 1700, LocalDateTime.now(), LocalDateTime.now());
        Product product2 = new Product(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 1800, LocalDateTime.now(), LocalDateTime.now());

        ProductStock productStock = new ProductStock(6L, product, 12, LocalDateTime.now(), LocalDateTime.now());
        ProductStock productStock2 = new ProductStock(7L, product2, 4, LocalDateTime.now(), LocalDateTime.now());

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(product.getId(), product.getName(), 3),
                new OrderRequestDto(product2.getId(), product2.getName(), 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product), Optional.of(product2));
        given(productStockRepository.findByProductIdWithLock(anyLong())).willReturn(productStock, productStock2);

        // when
        String orderNum = orderService.checkout(orderRequest);

        // then
        Assertions.assertThat(orderNum).isNotNull();
        verify(orderRepository, times(1)).save(any(Orders.class));
        verify(productStockRepository, times(2)).save(any(ProductStock.class));
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
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

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.checkout(orderRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EMPTY_USER_NAME.getMessage());
    }

    @Test
    @DisplayName("주문시 주문자 주소가 비어있을경우")
    public void checkout_when_empty_address() throws Exception {
        // given
        String userName = "김프레시";
        String address = null;

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(6L, "시티 오브 더 라이트 볼캡 [블랙]", 3),
                new OrderRequestDto(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.checkout(orderRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EMPTY_ADDRESS.getMessage());
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

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.checkout(orderRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EMPTY_PRODUCT_NAME.getMessage());
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

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.checkout(orderRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PRODUCT_QUANTITY.getMessage());
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

        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.checkout(orderRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_FOUND_PRODUCT.getMessage());
    }

    @Test
    @DisplayName("주문시 상품 재고가 부족한 경우")
    public void checkout_when_out_of_stock_product() throws Exception {
        // given
        String userName = "김프레시";
        String address = "서울특별시 송파구 송파대로 111 201동 803호";

        Product product = new Product(6L, "시티 오브 더 라이트 볼캡 [블랙]", 1700, LocalDateTime.now(), LocalDateTime.now());
        Product product2 = new Product(7L, "라이트웨이트 크루 삭스 5팩 [라이트 브라운]", 1800, LocalDateTime.now(), LocalDateTime.now());

        ProductStock productStock = new ProductStock(6L, product, 12, LocalDateTime.now(), LocalDateTime.now());

        List<OrderRequestDto> orderInfos = Arrays.asList(
                new OrderRequestDto(product.getId(), product.getName(), 15),
                new OrderRequestDto(product2.getId(), product2.getName(), 2)
        );
        OrderRequest orderRequest = new OrderRequest(userName, address, orderInfos);

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product), Optional.of(product2));
        given(productStockRepository.findByProductIdWithLock(anyLong())).willReturn(productStock);

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.checkout(orderRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.OUT_OF_STOCK.getMessage());
    }
}
