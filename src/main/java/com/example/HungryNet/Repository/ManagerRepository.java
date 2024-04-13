package com.example.HungryNet.Repository;

import com.example.HungryNet.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ManagerRepository extends JpaRepository<Manager, Integer> {
}
