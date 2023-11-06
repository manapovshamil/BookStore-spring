package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    List<Genre> findGenresByParentIdIsNull();
    List<Genre> findGenresByParentId(Integer id);
    List<Genre> findGenresByParentIdIn(List<Integer> id);
}
