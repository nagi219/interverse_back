package com.interverse.demo.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.interverse.demo.dto.OrderDetailDTO;
import com.interverse.demo.service.OrderDetailService;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {
    
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<OrderDetailDTO> createOrderDetail(@RequestBody OrderDetailDTO orderDetailDTO) {
        OrderDetailDTO createdOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
        return new ResponseEntity<>(createdOrderDetail, HttpStatus.CREATED);
    }
    
    @PostMapping("/bulk")
    public ResponseEntity<List<OrderDetailDTO>> createOrderDetails(@RequestBody List<OrderDetailDTO> orderDetailDTOs) {
        List<OrderDetailDTO> createdOrderDetails = new ArrayList<>();
        for (OrderDetailDTO dto : orderDetailDTOs) {
            createdOrderDetails.add(orderDetailService.createOrderDetail(dto));
        }
        return new ResponseEntity<>(createdOrderDetails, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderDetailDTO> getOrderDetail(
            @PathVariable Integer orderId, @PathVariable Integer productId) {
        OrderDetailDTO orderDetail = orderDetailService.getOrderDetailDTO(orderId, productId);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> getAllOrderDetails(@PathVariable Integer orderId) {
        List<OrderDetailDTO> orderDetails = orderDetailService.getAllOrderDetailDTOs(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    @DeleteMapping("/{orderId}/{productId}")
    public ResponseEntity<Void> deleteOrderDetail(
            @PathVariable Integer orderId, @PathVariable Integer productId) {
        orderDetailService.deleteOrderDetail(orderId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> deleteAllOrderDetails(@PathVariable Integer orderId) {
        orderDetailService.deleteAllOrderDetails(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/{orderId}/total")
    public ResponseEntity<Integer> calculateTotalAmount(@PathVariable Integer orderId) {
        Integer totalAmount = orderDetailService.calculateTotalAmount(orderId);
        return ResponseEntity.ok(totalAmount);
    }

    // 新增的方法，用於獲取單個訂單詳情的小計
    @GetMapping("/{orderId}/{productId}/subtotal")
    public ResponseEntity<Integer> getOrderDetailSubtotal(
            @PathVariable Integer orderId, @PathVariable Integer productId) {
        OrderDetailDTO orderDetail = orderDetailService.getOrderDetailDTO(orderId, productId);
        return ResponseEntity.ok(orderDetail.getSubtotal());
    }
}