package com.example.MyBookShopApp.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String description;
    private String photo;
    private String slug;

    @OneToMany
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    private List<Book> bookList = new ArrayList<>();

    public String toString(){
        return lastName + " " + firstName;
    }


}
