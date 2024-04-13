package com.example.HungryNet.Repository;

import com.example.HungryNet.Model.Menu;
import com.example.HungryNet.Model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    @Query(value = "select * from menu m join restaurant r on m.restaurant_id=r.id where r.id=:id and m.active=1", nativeQuery = true)
    List<Menu> findAllActiveMenu(int id);
}
