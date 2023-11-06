package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TagRepository extends JpaRepository<Tag, Integer>{

}
