package com.example.HungryNet.Model;

import com.example.HungryNet.Enumerator.Role;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    private Role role;

    @OneToMany(mappedBy = "client")
    private List<Order> order = new ArrayList<>();

}
