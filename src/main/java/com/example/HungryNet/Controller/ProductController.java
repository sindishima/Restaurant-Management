package com.example.HungryNet.Controller;

import com.example.HungryNet.Model.Product;
import com.example.HungryNet.Model.Restaurant;
import com.example.HungryNet.Repository.ProductRepository;
import com.example.HungryNet.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id){
        Optional<Product> prd = productRepository.findById(id);
        return new ResponseEntity<>(prd.get(),HttpStatus.OK);
    }

    @PostMapping("/restaurant/{id}")
    public ResponseEntity<Product> createProduct(@PathVariable Integer id, @RequestBody Product prd){
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        Restaurant res = restaurant.get();
        res.getProducts().add(prd);
        prd.setRestaurant(res);
        return new ResponseEntity<>(productRepository.save(prd), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product prd){
        Optional<Product> product = productRepository.findById(id);
        Product newProduct = product.get();
        newProduct.setName(prd.getName());
        newProduct.setAmount(prd.getAmount());
        newProduct.setCategory(prd.getCategory());
        newProduct.setImage(prd.getImage());
        newProduct.setPrice(prd.getPrice());
        return new ResponseEntity<>(productRepository.save(newProduct),HttpStatus.OK);
    }


//    @PutMapping("/{productId}/addRestaurnat/{restaurantId}")
//    public ResponseEntity<Product> addProductToRestaurant(@PathVariable Integer productId, @PathVariable Integer restaurantId){
//        Optional<Restaurant> res = restaurantRepository.findById(restaurantId);
//        Optional<Product> prd = productRepository.findById(productId);
//        Restaurant restaurant = res.get();
//        Product product = prd.get();
//
//        product.setRestaurant(restaurant);
//
//        return new ResponseEntity<>(productRepository.save(product),HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id){
        productRepository.deleteById(id);
    }
}
