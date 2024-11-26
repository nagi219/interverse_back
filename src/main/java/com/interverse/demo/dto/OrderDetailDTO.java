package com.interverse.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDTO {
    private Integer orderId; // 關聯的訂單ID
    private Integer productId;
    private Integer quantity;
    private Integer price;
    

    public Integer getSubtotal() {
        return price * quantity;
    }
}
