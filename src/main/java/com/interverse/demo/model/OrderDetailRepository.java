package com.interverse.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

    /**
     * 根據訂單ID查找所有訂單詳情
     * @param orderId 訂單ID
     * @return 訂單詳情列表
     */
    List<OrderDetail> findByOrderDetailIdOrdersId(Integer orderId);

    /**
     * 根據訂單ID刪除所有相關的訂單詳情
     * @param orderId 訂單ID
     */
    void deleteByOrderDetailIdOrdersId(Integer orderId);

    /**
     * 根據產品ID查找所有訂單詳情
     * @param productId 產品ID
     * @return 訂單詳情列表
     */
    List<OrderDetail> findByOrderDetailIdProductsId(Integer productId);

    /**
     * 根據訂單ID和產品ID查找特定的訂單詳情
     * @param orderId 訂單ID
     * @param productId 產品ID
     * @return 訂單詳情
     */
    OrderDetail findByOrderDetailIdOrdersIdAndOrderDetailIdProductsId(Integer orderId, Integer productId);

    /**
     * 檢查特定訂單中是否存在特定產品
     * @param orderId 訂單ID
     * @param productId 產品ID
     * @return 如果存在則返回true，否則返回false
     */
    boolean existsByOrderDetailIdOrdersIdAndOrderDetailIdProductsId(Integer orderId, Integer productId);

    /**
     * 計算特定訂單的詳情數量
     * @param orderId 訂單ID
     * @return 詳情數量
     */
    long countByOrderDetailIdOrdersId(Integer orderId);
}
