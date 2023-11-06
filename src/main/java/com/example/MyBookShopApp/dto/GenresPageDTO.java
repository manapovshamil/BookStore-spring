package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.entities.Genre;
import lombok.Data;

import java.util.List;
@Data
public class GenresPageDTO {
    private Integer count;
    private List<Genre> genres;

    public GenresPageDTO(List<Genre> genres) {
        this.count = genres.size();
        this.genres = genres;
    }
}
