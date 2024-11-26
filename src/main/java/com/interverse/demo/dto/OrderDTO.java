package com.interverse.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDTO {
    private Integer id;	
    private Integer paymentMethod;
    private Integer status;
    private LocalDateTime added;
    private Integer userId;
    private Integer totalAmount;
    private List<OrderDetailDTO> orderDetails;
    
    public OrderDTO() {
        this.orderDetails = new ArrayList<>();  // 初始化為空列表而不是 null
    }

    public void calculateTotalAmount() {
        if (orderDetails != null) {
            this.totalAmount = orderDetails.stream()
                    .mapToInt(OrderDetailDTO::getSubtotal)
                    .sum();
        } else {
            this.totalAmount = 0;
        }
    }
    
    public boolean hasOrderDetails() {
        return orderDetails != null && !orderDetails.isEmpty();
    }
}
