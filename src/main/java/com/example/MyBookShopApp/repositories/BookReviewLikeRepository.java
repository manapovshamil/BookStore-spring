package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.entities.BookReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Integer> {
}
