package com.example.HungryNet.Model;

import com.example.HungryNet.Controller.MenuController;
import com.example.HungryNet.Enumerator.Meal;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;
    private Meal meal;
    private boolean active;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private String startTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private String endTime;

    @JsonIgnore
    @ManyToMany(mappedBy = "menu")
    private List<Product> products = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return id == menu.id && active == menu.active && meal == menu.meal && Objects.equals(startTime, menu.startTime) && Objects.equals(endTime, menu.endTime) && Objects.equals(products, menu.products) && Objects.equals(restaurant, menu.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, meal, active, startTime, endTime, products, restaurant);
    }
}
