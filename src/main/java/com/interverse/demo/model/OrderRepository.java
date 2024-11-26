package com.interverse.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUsersId(Integer userId);
    List<Order> findByStatus(Integer status);
    List<Order> findByAddedBetween(LocalDateTime start, LocalDateTime end);
    Page<Order> findByUsersId(Integer userId, Pageable pageable);
    List<Order> findByStatusAndAddedBetween(Integer status, LocalDateTime start, LocalDateTime end);
    List<Order> findByUsersIdAndAddedBetween(Integer userId, LocalDateTime start, LocalDateTime end);
    List<Order> findByPaymentMethod(Integer paymentMethod);
    
    @Query("SELECT o FROM Order o ORDER BY o.added DESC")
    List<Order> findRecentOrders(Pageable pageable);
    
    long countByStatus(Integer status);
    Order findFirstByUsersIdOrderByAddedDesc(Integer userId);
    
    @Query("SELECT o FROM Order o WHERE o.added BETWEEN :start AND :end AND o.status = :status AND o.paymentMethod = :paymentMethod")
    List<Order> findOrdersByConditions(@Param("start") LocalDateTime start, 
                                       @Param("end") LocalDateTime end,
                                       @Param("status") Integer status, 
                                       @Param("paymentMethod") Integer paymentMethod);
}
