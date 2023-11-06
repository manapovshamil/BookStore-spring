package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.entities.RateBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RateBookRepository extends JpaRepository<RateBook, Integer> {

    @Query(value = "select * from rate_books where book_id = :bookId and user_id = :userId",
           countQuery = "select COUNT(*) from rate_books where book_id = :bookId and user_id = :userId",
           nativeQuery = true)
     RateBook getRateBook(Integer bookId, String userId);
    List<RateBook> findRateBookByBookId(Integer bookId);
}
