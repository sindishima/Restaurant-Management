package com.example.HungryNet.Services;

import com.example.HungryNet.Enumerator.OrderStatus;
import com.example.HungryNet.Exceptions.ResourceNotFoundException;
import com.example.HungryNet.Model.*;
import com.example.HungryNet.Repository.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RestaurantRepository restaurantRepository;


    private DateFormat formatter = new SimpleDateFormat("hh:mm aa");


    public List<OrderProducts> getOrderProducts(Integer orderId){
        Optional<Order> orders = orderRepository.findById(orderId);
        List<OrderProducts> orderProducts = orders.get().getOrderProducts();
        return orderProducts;
    }

    public Order createOrder(Integer restaurantId, Integer prdId, Integer clientId, Integer amount) throws ParseException {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        Restaurant restaurant = restaurantOptional.get();
        List<Menu> menu = restaurantOptional.get().getMenu();

        Menu menu1 = new Menu();
        for (Menu m : menu) {
            if(m.isActive()){
                menu1=m;
            }
        }

        System.out.println("menu "+menu1.getName());


        Optional<Product> productOptional = productRepository.findById(prdId);
        Product product = productOptional.get();

        if(menu1.getProducts().contains(product)){
            Optional<Client> clientOptional = clientRepository.findById(clientId);
            Client client = clientOptional.get();

            Date start = formatter.parse(menu1.getStartTime());
            Date end = formatter.parse(menu1.getEndTime());
            String date = formatter.format(new Date());
            Date now = formatter.parse(date);

            Order order = new Order();
            OrderProducts orderProduct = new OrderProducts();

            Double price = amount * product.getPrice();
            orderProduct.setPrice(price);
            orderProduct.setAmount(amount);
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setProductName(product.getName());

            order.setRestaurant(restaurant);
            order.setClient(client);
            order.getOrderProducts().add(orderProduct);
            order.setCreatedTime(new Date());
            order.setStatus(OrderStatus.CREATED);

            client.getOrder().add(order);

            restaurant.getOrders().add(order);

            Integer prdAmount = product.getAmount() - orderProduct.getAmount();
            product.setAmount(prdAmount);
            productRepository.save(product);
            orderRepository.save(order);
            return order;
        } else {
            throw new ResourceNotFoundException("Product with id: " + prdId + " does not exist in restaurant with id: " + restaurantId);
        }
    }

    public Order addProductToOrder(Integer orderId, Integer prdId, Integer amount, Integer menuId){
        Optional<Menu> menuOptional = menuRepository.findById(menuId);
        Menu menu = menuOptional.get();
        Optional<Product> productOptional = productRepository.findById(prdId);
        Product product = productOptional.get();
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.get();

        Restaurant restaurant = menu.getRestaurant();
        Client client = order.getClient();

        OrderProducts orderProduct = new OrderProducts();

        Double price = amount * product.getPrice();
        orderProduct.setPrice(price);
        orderProduct.setAmount(amount);
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setProductName(product.getName());

        order.getOrderProducts().add(orderProduct);

        Integer prdAmount = product.getAmount() - orderProduct.getAmount();
        product.setAmount(prdAmount);
        productRepository.save(product);
        orderRepository.save(order);

        return order;
    }

    public Order removeProductFromOrder(Integer orderId, Integer prdId){
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.get();
        Optional<Product> productOptional = productRepository.findById(prdId);
        Product product = productOptional.get();

        order.getOrderProducts().remove(product);
        product.getOrderProducts().remove(product);

        return order;
    }

    public String deleteOrder(Integer orderId){
        String message = "Order deleted!";
        Optional<Order> order = orderRepository.findById(orderId);
        orderRepository.deleteById(orderId);
        return message;
    }
}
