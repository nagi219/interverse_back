package com.interverse.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.interverse.demo.dto.OrderDetailDTO;
import com.interverse.demo.model.Order;
import com.interverse.demo.model.OrderDetail;
import com.interverse.demo.model.OrderDetailId;
import com.interverse.demo.model.OrderDetailRepository;
import com.interverse.demo.model.OrderRepository;
import com.interverse.demo.model.Product;
import com.interverse.demo.model.ProductRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderDetailService {
    
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    public OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        
        Product product = productRepository.findById(orderDetailDTO.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        OrderDetail detail = new OrderDetail();
        OrderDetailId id = new OrderDetailId(orderDetailDTO.getOrderId(), orderDetailDTO.getProductId());
        detail.setOrderDetailId(id);
        detail.setQuantity(orderDetailDTO.getQuantity());
        detail.setOrders(order);
        detail.setProducts(product);
        OrderDetail savedDetail = orderDetailRepository.save(detail);
        return convertToDTO(savedDetail);
    }

    public OrderDetailDTO getOrderDetailDTO(Integer orderId, Integer productId) {
        OrderDetailId id = new OrderDetailId(orderId, productId);
        OrderDetail detail = orderDetailRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found"));
        
        return convertToDTO(detail);
    }

    public List<OrderDetailDTO> getAllOrderDetailDTOs(Integer orderId) {
        List<OrderDetail> details = orderDetailRepository.findByOrderDetailIdOrdersId(orderId);
        return details.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void deleteOrderDetail(Integer orderId, Integer productId) {
        OrderDetailId id = new OrderDetailId(orderId, productId);
        orderDetailRepository.deleteById(id);
    }

    public void deleteAllOrderDetails(Integer orderId) {
        orderDetailRepository.deleteByOrderDetailIdOrdersId(orderId);
    }

    public Integer calculateTotalAmount(Integer orderId) {
        return getAllOrderDetailDTOs(orderId).stream()
                .mapToInt(OrderDetailDTO::getSubtotal)
                .sum();
    }

    private OrderDetailDTO convertToDTO(OrderDetail detail) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setOrderId(detail.getOrderDetailId().getOrdersId());
        dto.setProductId(detail.getOrderDetailId().getProductsId());
        dto.setQuantity(detail.getQuantity());
        dto.setPrice(detail.getProducts().getPrice());
        // 不需要在這裡設置 subtotal，因為 OrderDetailDTO 中的 getSubtotal() 方法會自動計算
        return dto;
    }
}