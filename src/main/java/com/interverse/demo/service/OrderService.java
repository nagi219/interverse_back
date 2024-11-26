package com.interverse.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interverse.demo.dto.CartResponseDTO;
import com.interverse.demo.dto.OrderDTO;
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
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    
    @Transactional
    public List<OrderDetailDTO> createOrderWithDetails(Integer orderId, List<CartResponseDTO> cartItems) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        List<OrderDetailDTO> createdOrderDetails = new ArrayList<>();

        for (CartResponseDTO cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            OrderDetail detail = new OrderDetail();
            OrderDetailId id = new OrderDetailId(orderId, cartItem.getProductId());
            detail.setOrderDetailId(id);
            detail.setQuantity(cartItem.getVol());
            detail.setOrders(order);
            detail.setProducts(product);

            OrderDetail savedDetail = orderDetailRepository.save(detail);
            createdOrderDetails.add(convertToDTO(savedDetail, cartItem.getPrice()));
        }

        return createdOrderDetails;
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setStatus(orderDTO.getStatus());
        order.setUsers(userService.findUserById(orderDTO.getUserId()));
        order = orderRepository.save(order);

        // 創建訂單詳情
        for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
            detailDTO.setOrderId(order.getId());
            orderDetailService.createOrderDetail(detailDTO);
        }

        // 轉換為 DTO 並返回
        return convertToDTO(order);
    }

    public OrderDTO getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return convertToDTO(order);
    }

    public List<OrderDTO> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUsersId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<OrderDTO> getOrdersByUserIdPaginated(Integer userId, Pageable pageable) {
        return orderRepository.findByUsersId(userId, pageable)
                .map(this::convertToDTO);
    }

    public List<OrderDTO> getOrdersByStatus(Integer status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByAddedBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO updateOrderStatus(Integer orderId, Integer newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(newStatus);
        order = orderRepository.save(order);
        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO updateOrderPaymentMethod(Integer orderId, Integer newPaymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setPaymentMethod(newPaymentMethod);
        order = orderRepository.save(order);
        return convertToDTO(order);
    }

    @Transactional
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<OrderDTO> getOrdersByPaymentMethod(Integer paymentMethod) {
        return orderRepository.findByPaymentMethod(paymentMethod).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getRecentOrders(Pageable pageable) {
        return orderRepository.findRecentOrders(pageable).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long countOrdersByStatus(Integer status) {
        return orderRepository.countByStatus(status);
    }

    public OrderDTO getLastOrderByUser(Integer userId) {
        Order order = orderRepository.findFirstByUsersIdOrderByAddedDesc(userId);
        return order != null ? convertToDTO(order) : null;
    }

    public List<OrderDTO> searchOrders(LocalDateTime start, LocalDateTime end, Integer status, Integer paymentMethod) {
        return orderRepository.findOrdersByConditions(start, end, status, paymentMethod).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUsers().getId());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setAdded(order.getAdded());
        
        // 獲取訂單詳情
        List<OrderDetailDTO> orderDetails = orderDetailService.getAllOrderDetailDTOs(order.getId());
        dto.setOrderDetails(orderDetails);
        
        // 計算總金額
        int totalAmount = orderDetails.stream()
                .mapToInt(OrderDetailDTO::getSubtotal)
                .sum();
        dto.setTotalAmount(totalAmount);
        
        return dto;
    }
   
 
   
    
    private OrderDetailDTO convertToDTO(OrderDetail orderDetail, Integer price) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setOrderId(orderDetail.getOrderDetailId().getOrdersId().intValue());
        dto.setProductId(orderDetail.getOrderDetailId().getProductsId().intValue());
        dto.setQuantity(orderDetail.getQuantity());
        dto.setPrice(price);  // 使用 CartResponseDTO 中的价格
        return dto;
    }
    
}
