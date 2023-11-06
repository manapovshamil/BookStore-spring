package com.example.MyBookShopApp.dto;

import lombok.Data;

@Data
public class TagDTO {

    private String name;
    private Integer count;

    public TagDTO(String name, Integer count) {
        this.name = name;
        this.count = count;
    }

}
