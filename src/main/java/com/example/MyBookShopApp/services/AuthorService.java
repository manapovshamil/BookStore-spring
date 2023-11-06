package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entities.Author;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    public List<Author> getAuthorsData(){
        return authorRepository.findAll();
    }
    public List<Author> getAuthorInfo(Integer authorId){
        return authorRepository.findAuthorById(authorId);
    }

}
