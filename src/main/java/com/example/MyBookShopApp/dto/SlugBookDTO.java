package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.entities.*;
import lombok.Data;
@Data
public class SlugBookDTO {
    Book book;
    int rating;
    int rateCount;
    int currentUserRating;
    int rate1, rate2, rate3, rate4, rate5;
}
