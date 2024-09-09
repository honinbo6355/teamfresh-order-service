package com.teamfresh.example.domain.orders;

import com.teamfresh.example.api.orders.dto.OrderRequest;
import com.teamfresh.example.domain.product.Product;
import com.teamfresh.example.domain.product.ProductRepository;
import com.teamfresh.example.domain.product.ProductStock;
import com.teamfresh.example.domain.product.ProductStockRepository;
import com.teamfresh.example.exception.CustomException;
import com.teamfresh.example.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public String checkout(OrderRequest orderRequest) {
        orderRequest.validate();

        Orders order = new Orders(orderRequest.userName(), orderRequest.address());

        List<OrderItem> orderItems = orderRequest.orderInfos().stream()
                .map(orderInfo -> {
                    Product product = productRepository.findById(orderInfo.productId())
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
                    OrderItem orderItem = new OrderItem(product, orderInfo.quantity());
                    order.addItem(orderItem);
                    return orderItem;
                })
                .toList();

        Orders savedOrder = orderRepository.save(order);

        orderItems.forEach(orderItem -> {
            ProductStock productStock = productStockRepository.findByProductIdWithLock(orderItem.getProduct().getId());
            productStock.decreaseStock(orderItem.getQuantity());

            productStock.setProduct(orderItem.getProduct());
            orderItem.setOrder(savedOrder);

            productStockRepository.save(productStock);
            orderItemRepository.save(orderItem);
        });

        return order.getOrderNum();
    }
}
