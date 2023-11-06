package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.entities.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFile, Integer> {

    public BookFile findBookFileByHash(String hash);
}
