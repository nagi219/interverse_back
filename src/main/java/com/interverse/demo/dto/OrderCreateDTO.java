package com.interverse.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {
    private Integer orderId;
    private List<CartResponseDTO> cartItems;
}
