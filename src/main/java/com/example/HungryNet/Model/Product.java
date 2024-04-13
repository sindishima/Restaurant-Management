package com.example.HungryNet.Model;

import com.example.HungryNet.Enumerator.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id ;

    @NotNull
    private String name ;

    @NotNull
    private Category category;

    @NotNull
    private Double price ;

    @NotNull
    private Integer amount ;

    private String image ;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_menu",
            joinColumns =
                    {@JoinColumn(name = "menu_id")},
            inverseJoinColumns =
                    {@JoinColumn(name = "product_id")})
    @JsonIgnore
    private List<Menu> menu;

    @OneToMany(mappedBy = "product")
    private List<OrderProducts> orderProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;
}
