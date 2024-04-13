package com.example.HungryNet.Repository;

import com.example.HungryNet.Model.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface OrderProductsRepository extends JpaRepository<OrderProducts, Integer> {
}