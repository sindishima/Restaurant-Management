package com.example.HungryNet.Repository;

import com.example.HungryNet.Model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
