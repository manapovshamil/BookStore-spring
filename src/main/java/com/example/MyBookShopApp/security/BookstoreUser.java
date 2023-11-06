package com.example.MyBookShopApp.security;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class BookstoreUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String password;

}
