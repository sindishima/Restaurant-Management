package com.example.HungryNet.Controller;

import com.example.HungryNet.Model.Admin;
import com.example.HungryNet.Model.Manager;
import com.example.HungryNet.Model.Product;
import com.example.HungryNet.Model.Restaurant;
import com.example.HungryNet.Repository.ManagerRepository;
import com.example.HungryNet.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ManagerRepository managerRepository;

    @GetMapping
    public List<Restaurant> getRestaurant(){
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Integer id){
        Optional<Restaurant> res=restaurantRepository.findById(id);
        return new ResponseEntity<>(res.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}/manager")
    public ResponseEntity<Manager> getRestaurantManager(@PathVariable Integer id){
        Optional<Restaurant> res = restaurantRepository.findById(id);
        Manager manager = res.get().getManager();
        return new ResponseEntity<>(manager, HttpStatus.OK);
    }

    @GetMapping("/{id}/products")
    public List<Product> getProductsOfRestaurant(@PathVariable Integer id){
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        List<Product> prd = restaurant.get().getProducts();
        return prd;
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant res){
        return new ResponseEntity<>(restaurantRepository.save(res), HttpStatus.CREATED);
    }

//    @PostMapping("/{restaurantId}/addManager/{managerId}")
//    public ResponseEntity<Restaurant> addManager(@PathVariable Integer restaurantId, @PathVariable Integer managerId){
//        Optional<Manager> manager = managerRepository.findById(managerId);
//        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
//        Restaurant res = restaurant.get();
//        Manager man = manager.get();
//        man.setRestaurant(res);
//        res.setManager(man);
//
//        return new ResponseEntity<>(restaurantRepository.save(res), HttpStatus.CREATED);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Integer id, @RequestBody Restaurant res){
        Optional<Restaurant> restaurant1 = restaurantRepository.findById(id);
        Restaurant restaurant = restaurant1.get();
        restaurant.setName(res.getName());
        restaurant.setAddress(res.getAddress());
        return new ResponseEntity<>(restaurantRepository.save(restaurant), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteRestaurant(@PathVariable Integer id){
        restaurantRepository.deleteById(id);
    }

}
