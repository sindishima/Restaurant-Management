package com.example.HungryNet.Repository;

import com.example.HungryNet.Enumerator.Role;
import com.example.HungryNet.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface AdminRepository extends JpaRepository<Admin, Integer> {
    @Query(value = "select * from admin a where a.role = 0", nativeQuery = true)
    List<Admin> findAllByRole();

    Admin findByUsername(String username);
}
