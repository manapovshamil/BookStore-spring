package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.entities.Genre;
import lombok.Data;

import java.util.List;

@Data
public class GenreDTO {
    private Integer id;
    private Integer parentId;
    private String slug;
    private String name;
    private List<Genre> child;
}
