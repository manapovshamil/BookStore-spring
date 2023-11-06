package com.example.MyBookShopApp.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id",referencedColumnName = "id")
    private List<Book> bookList = new ArrayList<>();

}
