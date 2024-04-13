package com.example.HungryNet.Controller;

import com.example.HungryNet.Enumerator.OrderStatus;
import com.example.HungryNet.Enumerator.Role;
import com.example.HungryNet.Model.*;
import com.example.HungryNet.Repository.ManagerRepository;
import com.example.HungryNet.Repository.OrderRepository;
import com.example.HungryNet.Repository.ProductRepository;
import com.example.HungryNet.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    RestaurantRepository restaurantsRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public List<Manager> getManager(){
        return managerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Integer id){
        Optional<Manager> manager = managerRepository.findById(id);
        return new ResponseEntity<>(manager.get(), HttpStatus.OK);
    }

    @GetMapping("/{managerId}/order")
    public List<Order> getMyOrders(@PathVariable Integer managerId){
        Optional<Manager> managerOptional = managerRepository.findById(managerId);
        Manager manager = managerOptional.get();
        List<Order> order = manager.getRestaurant().getOrders();
        return order;
    }

    @GetMapping("/{managerId}/orders")
    public List<Order> getMyOrdersFilteredByStatus(@PathVariable Integer managerId, @RequestParam("status") OrderStatus status){
        Optional<Manager> managerOptional = managerRepository.findById(managerId);
        Manager manager = managerOptional.get();
        List<Order> order = manager.getRestaurant().getOrders();
        ArrayList<Order> order1 = new ArrayList<Order>();
        for(Order o : order){
            if(o.getStatus().equals(status)){
                order1.add(o);
            }
        }
        return order1;
    }


    @PostMapping("/restaurant/{id}")
    public ResponseEntity<Manager> createManager(@PathVariable Integer id, @RequestBody Manager manager){
        Optional<Restaurant> restaurant = restaurantsRepository.findById(id);
        manager.setRestaurant(restaurant.get());
        manager.setRole(Role.MANAGER);
        return new ResponseEntity<>(managerRepository.save(manager), HttpStatus.OK);
    }


    @PutMapping("/order/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Integer orderId, @RequestParam("status") String status){
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.get();

        OrderStatus orderStatus = OrderStatus.valueOf(status);
        order.setStatus(orderStatus);

        if(!order.getStatus().equals(OrderStatus.REJECTED)) {
            if (orderStatus.equals(OrderStatus.REJECTED)) {
                List<OrderProducts> orderProducts = order.getOrderProducts();
                for (OrderProducts n : orderProducts) {
                    int amount = n.getAmount();
                    Optional<Product> prd = productRepository.findById(n.getProduct().getId());
                    Product product = prd.get();
                    product.setAmount(product.getAmount() + amount);
                }
            }
        }
        return new ResponseEntity<>(orderRepository.save(order), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Manager> updateManager(@PathVariable Integer id, @RequestBody Manager man){
        Optional<Manager> oldManager = managerRepository.findById(id);
        Manager manager = oldManager.get();
        manager.setUsername(man.getUsername());
        manager.setPassword(man.getPassword());
        manager.setRole(man.getRole());
        manager.setRestaurant(manager.getRestaurant());
        return new ResponseEntity<>(managerRepository.save(manager), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Integer id){
        managerRepository.deleteById(id);
    }

}
