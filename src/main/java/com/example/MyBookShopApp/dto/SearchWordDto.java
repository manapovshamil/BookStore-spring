package com.example.MyBookShopApp.dto;

import lombok.Data;

@Data
public class SearchWordDto {
    private String text;

    public SearchWordDto(String example) {
        this.text = example;
    }

    public SearchWordDto(){}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
