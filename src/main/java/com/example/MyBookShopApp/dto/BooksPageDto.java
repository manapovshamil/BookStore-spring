package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.entities.Book;
import lombok.Data;

import java.util.List;
@Data
public class BooksPageDto {
    private Integer count;
    private List<Book> books;

    public BooksPageDto(List<Book> books) {
        this.count = books.size();
        this.books = books;
    }
}
