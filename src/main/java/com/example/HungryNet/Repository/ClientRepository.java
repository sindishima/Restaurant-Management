package com.example.HungryNet.Repository;

import com.example.HungryNet.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ClientRepository extends JpaRepository<Client, Integer> {
}
