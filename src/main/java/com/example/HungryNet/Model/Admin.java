package com.example.HungryNet.Model;

import com.example.HungryNet.Enumerator.Role;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Setter
@Getter
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username cannot be null!")
    @Size(max = 10)
    private String username;

    @NotBlank(message = "Password cannot be null!")
    private String password;

    @NotNull
    private Role role;

}
