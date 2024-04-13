package com.example.HungryNet.Model;

import com.example.HungryNet.Enumerator.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Manager")
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    private Role role;

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;

//    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
////    @JoinTable(name = "manager_restaurant",
////            joinColumns =
////                    {@JoinColumn(name = "manager_id")},
////            inverseJoinColumns =
////                    {@JoinColumn(name = "restaurant_id")})
//    @JsonIgnore
//    private Restaurant restaurant;

}
