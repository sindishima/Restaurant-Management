package com.example.HungryNet.Controller;

import com.example.HungryNet.Enumerator.OrderStatus;
import com.example.HungryNet.Model.*;
import com.example.HungryNet.Repository.ClientRepository;
import com.example.HungryNet.Repository.MenuRepository;
import com.example.HungryNet.Repository.OrderRepository;
import com.example.HungryNet.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientRepository clientRepo;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    OrderRepository orderRepository;


    @GetMapping
    public List<Client> getClients(){
        return clientRepo.findAll();
    }

    private DateFormat formatter = new SimpleDateFormat("hh:mm aa");

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<?> getActiveMenu(@PathVariable Integer id) throws ParseException {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        Restaurant restaurant = restaurantOptional.get();
        List<Menu> menu = restaurantOptional.get().getMenu();
        for (Menu m : menu) {
            Date start = formatter.parse(m.getStartTime());
            Date end = formatter.parse(m.getEndTime());

            String now = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
            String now2 = new SimpleDateFormat("HH:mm").format(start);
            String now3 = new SimpleDateFormat("HH:mm").format(end);

            LocalTime target = LocalTime.parse(now);
            LocalTime start1 = LocalTime.parse(now2);
            LocalTime end1 = LocalTime.parse(now3);
            System.out.println("Strt"+start1);
            System.out.println("End"+end1);

            if(target.isAfter(start1) && target.isBefore(end1)){
                Menu menu1 = m;
                menu1.setActive(true);
                menuRepository.save(menu1);
                System.out.println("Acitve menu "+menu1.getName());
            }
        }
        return new ResponseEntity<>(menuRepository.findAllActiveMenu(id), HttpStatus.OK);
    }

    @GetMapping("/{clientId}/orders")
    public List<Order> getMyOrders(@PathVariable Integer clientId){
        return orderRepository.findOrderByClient_Id(clientId);
    }

    @GetMapping("/{clientId}/order/{orderId}")
    public OrderStatus getOrderStatus(@PathVariable Integer clientId, @PathVariable Integer orderId){
        Order myOrder = orderRepository.findOrderByStatus(clientId, orderId);
        System.out.println(myOrder.getStatus());
        return myOrder.getStatus();
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client){
        return new ResponseEntity<>(clientRepo.save(client), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Integer id, @RequestBody Client client){
        Optional<Client> cl=clientRepo.findById(id);
        Client newClient=cl.get();
        newClient.setUsername(client.getUsername());
        newClient.setPassword(client.getPassword());
        newClient.setRole(client.getRole());
        return new ResponseEntity<>(createClient(newClient).getBody(),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Integer id){
        clientRepo.deleteById(id);
    }

}
