package com.example.ecommercebackend.Modules;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    private String username;

    @Email
    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "appUser",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Address> addresses=new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "appUser",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private List<Product> products=new ArrayList<>();

    @ToString.Exclude
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Cart cart;


    public AppUser(String user1, String mail, String password1) {
        this.username = user1;
        this.email = mail;
        this.password = password1;
    }

    public AppUser() {

    }
}
