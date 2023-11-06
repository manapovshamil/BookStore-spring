package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findAuthorById(Integer authorId);
}
