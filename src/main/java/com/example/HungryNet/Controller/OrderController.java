package com.example.HungryNet.Controller;

import com.example.HungryNet.Exceptions.ResourceNotFoundException;
import com.example.HungryNet.Model.*;
import com.example.HungryNet.Repository.MenuRepository;
import com.example.HungryNet.Repository.OrderRepository;
import com.example.HungryNet.Repository.ProductRepository;
import com.example.HungryNet.Repository.RestaurantRepository;
import com.example.HungryNet.Services.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @GetMapping
    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id){
        Optional<Order> order = orderRepository.findById(id);
        return new ResponseEntity<>(order.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}/products")
    public List<OrderProducts> getOrderProducts(@PathVariable Integer id){
        return orderService.getOrderProducts(id);
    }


    @PostMapping("/restaurant/{restaurantId}/product/{productId}/client/{clientId}")
    public ResponseEntity<Order> createOrder(@PathVariable Integer restaurantId, @PathVariable Integer productId, @PathVariable Integer clientId, @RequestParam(name = "amount") Integer amount) throws ParseException {
        return new ResponseEntity<>(orderService.createOrder(restaurantId, productId, clientId, amount), HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/menu/{menuId}/product/{productId}")
    public ResponseEntity<Order> addProductToOrder(@PathVariable Integer orderId, @PathVariable Integer menuId, @PathVariable Integer productId, @RequestParam(name = "amount") Integer amount){
        return new ResponseEntity<>(orderService.addProductToOrder(orderId, productId, amount, menuId), HttpStatus.CREATED);
    }


    @DeleteMapping("/{orderId}/product/{prdId}")
    public ResponseEntity<Order> removeProductFromOrder(@PathVariable Integer prdId, @PathVariable Integer orderId){
        return new ResponseEntity<>(orderService.removeProductFromOrder(orderId, prdId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer orderId){
        return new ResponseEntity<>(orderService.deleteOrder(orderId), HttpStatus.OK);
    }
}
