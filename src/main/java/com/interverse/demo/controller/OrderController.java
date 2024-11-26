package com.interverse.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interverse.demo.dto.OrderCreateDTO;
import com.interverse.demo.dto.OrderDTO;
import com.interverse.demo.dto.OrderDetailDTO;
import com.interverse.demo.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    
    @PostMapping("/create-with-details")
    public ResponseEntity<List<OrderDetailDTO>> createOrderWithDetails(@RequestBody OrderCreateDTO OrderCreateDTO) {
        List<OrderDetailDTO> createdOrderDetails = orderService.createOrderWithDetails(
        	OrderCreateDTO.getOrderId(), 
        	OrderCreateDTO.getCartItems()
        	
        );
        return new ResponseEntity<>(createdOrderDetails, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Integer id) {
        OrderDTO orderDTO  = orderService.getOrderById(id);
        if (!orderDTO.hasOrderDetails()) {
            // 如果沒有訂單詳情，可以選擇在這裡加載
            // 或者返回一個標記，表示詳情需要單獨加載
            orderDTO.setOrderDetails(null);  // 明確設置為 null，表示詳情未加載
        } else {
            orderDTO.calculateTotalAmount();  // 只在有詳情時計算總額
        }
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Integer userId) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<Page<OrderDTO>> getOrdersByUserPaginated(
            @PathVariable Integer userId, Pageable pageable) {
        Page<OrderDTO> orders = orderService.getOrdersByUserIdPaginated(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable Integer status) {
        List<OrderDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/dateRange")
    public ResponseEntity<List<OrderDTO>> getOrdersByDateRange(
            @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<OrderDTO> orders = orderService.getOrdersByDateRange(start, end);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/paymentMethod/{paymentMethod}")
    public ResponseEntity<List<OrderDTO>> getOrdersByPaymentMethod(@PathVariable Integer paymentMethod) {
        List<OrderDTO> orders = orderService.getOrdersByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<OrderDTO>> getRecentOrders(Pageable pageable) {
        List<OrderDTO> orders = orderService.getRecentOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countOrdersByStatus(@PathVariable Integer status) {
        long count = orderService.countOrdersByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/user/{userId}/last")
    public ResponseEntity<OrderDTO> getLastOrderByUser(@PathVariable Integer userId) {
        OrderDTO order = orderService.getLastOrderByUser(userId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderDTO>> searchOrders(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam Integer status,
            @RequestParam Integer paymentMethod) {
        List<OrderDTO> orders = orderService.searchOrders(start, end, status, paymentMethod);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Integer id, @RequestParam Integer newStatus) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }	

    @PutMapping("/{id}/paymentMethod")
    public ResponseEntity<OrderDTO> updateOrderPaymentMethod(
            @PathVariable Integer id, @RequestParam Integer newPaymentMethod) {
        OrderDTO updatedOrder = orderService.updateOrderPaymentMethod(id, newPaymentMethod);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
