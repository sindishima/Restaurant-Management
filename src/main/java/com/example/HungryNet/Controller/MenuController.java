package com.example.HungryNet.Controller;

import com.example.HungryNet.Exceptions.ResourceNotFoundException;
import com.example.HungryNet.Model.Menu;
import com.example.HungryNet.Model.Product;
import com.example.HungryNet.Model.Restaurant;
import com.example.HungryNet.Repository.MenuRepository;
import com.example.HungryNet.Repository.ProductRepository;
import com.example.HungryNet.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Menu> getMenu(){
        return menuRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable Integer id){
        Optional<Menu> menu = menuRepository.findById(id);
        return new ResponseEntity<>(menu.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}/products")
    public List<Product> getMenuProducts(@PathVariable Integer id){
        Optional<Menu> menu = menuRepository.findById(id);
        List<Product> prd = menu.get().getProducts();
        return prd;
    }


    private DateFormat formatter = new SimpleDateFormat("hh:mm aa");

    @PostMapping("/restaurant/{restaurantId}/product/{prdId}")
    public ResponseEntity<Menu> createMenu(@PathVariable Integer prdId, @PathVariable Integer restaurantId, @RequestBody Menu menu) throws ParseException{
        Optional<Product> prd = productRepository.findById(prdId);
        Product product = prd.get();
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        Restaurant res = restaurant.get();
        Date start = formatter.parse(menu.getStartTime());
        Date end = formatter.parse(menu.getEndTime());
        System.out.println(start);
        System.out.println(end);

        String now = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String now2 = new SimpleDateFormat("HH:mm").format(start);
        String now3 = new SimpleDateFormat("HH:mm").format(end);


//        LocalTime target = LocalTime.parse(now);
//        LocalTime start1 = LocalTime.parse(now2);
//        LocalTime end1 = LocalTime.parse(now3);
//        System.out.println("Strt"+start1);
//        System.out.println("End"+end1);
//
//        Boolean targetInZone = (
//                target.isAfter(start1)
//                        &&
//                        target.isBefore(end1)
//        ) ;
//
//        menu.setActive(targetInZone);
//        System.out.println("Target" + targetInZone);
//        System.out.println("Target"+target);

        if(res.getProducts().contains(product)){
           product.getMenu().add(menu);
           res.getMenu().add(menu);
           menu.getProducts().add(product);
           menu.setRestaurant(res);
       }
       else{
           throw new ResourceNotFoundException("Product with id "+prdId+" isnt part of this restaurant");
       }
        return new ResponseEntity<>(menuRepository.save(menu), HttpStatus.CREATED);
    }

    @PostMapping("/{menuId}/product/{productId}")
    public ResponseEntity<Menu> addProductsToMenu(@PathVariable Integer menuId, @PathVariable Integer productId){
        Optional<Menu> menu = menuRepository.findById(menuId);
        Menu menu1 = menu.get();
        Optional<Product> prd = productRepository.findById(productId);
        Product product = prd.get();
        menu1.getProducts().add(product);
        product.getMenu().add(menu1);
        return new ResponseEntity<>(menuRepository.save(menu1),HttpStatus.CREATED);
    }


    @PutMapping("/{menuId}")
    private ResponseEntity<Menu> updateMenu(@PathVariable Integer menuId, @RequestBody Menu menu){
        Optional<Menu> menu2 = menuRepository.findById(menuId);
        Menu menu1 = menu2.get();
        menu1.setName(menu.getName());
        menu1.setStartTime(menu.getStartTime());
        menu1.setEndTime(menu.getEndTime());

        return new ResponseEntity<>(menuRepository.save(menu1), HttpStatus.CREATED);
    }


    @DeleteMapping("/{menuId}/product/{productId}")
    public void removeProductFromMenu(@PathVariable Integer menuId, @PathVariable Integer productId){
        Optional<Menu> menu = menuRepository.findById(menuId);
        Menu menu1 = menu.get();
        Optional<Product> prd = productRepository.findById(productId);
        Product product = prd.get();

        product.getMenu().remove(menu1);
        menu1.getProducts().remove(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id){
        menuRepository.deleteById(id);
    }
}
