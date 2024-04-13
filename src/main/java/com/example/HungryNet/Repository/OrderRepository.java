package com.example.HungryNet.Repository;

import com.example.HungryNet.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value="select * from orders o where o.client_id=:id order by o.created_time desc", nativeQuery = true)
    List<Order> findOrderByClient_Id(int id);

    @Query(value = "select * from client c join orders o on c.id=o.client_id where client_id=:clientId and o.id=:orderId", nativeQuery = true)
    Order findOrderByStatus(int clientId, int orderId);
}
