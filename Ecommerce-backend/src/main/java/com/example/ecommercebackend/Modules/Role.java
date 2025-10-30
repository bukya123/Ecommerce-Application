package com.example.ecommercebackend.Modules;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "Roles")
 public class Role {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "role_id")
     private Integer roleId;


     @Enumerated(EnumType.STRING)
     @Column(length = 20, name = "role_name")
     private AppRole roleName;


 }
