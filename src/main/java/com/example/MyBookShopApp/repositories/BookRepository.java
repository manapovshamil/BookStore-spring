package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    List<Book> findBooksByAuthor_FirstName(String name);

    @Query("from Book")
    List<Book> customFindAllBooks();

    List<Book> findBooksByAuthorFirstNameContaining(String authorsFirstName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    List<Book> findBooksByPriceOldIs(Integer price);

    @Query("from Book where isBesteller=true")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books) ORDER BY id",
            countQuery = "SELECT COUNT(*) FROM books WHERE discount = (SELECT MAX(discount) FROM books)",
            nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    Page<Book> findBookByPubDateIsBetweenOrderByPubDate(Date dateFrom, Date dateTo, Pageable nextPage);

    @Query(value = "SELECT * FROM books where rating > 6 order by number_of_users_bought_book desc, number_of_users_basket_book desc, number_of_users_postponed_book desc",
            countQuery = "SELECT COUNT(*) FROM books where rating > 6",
            nativeQuery = true)
    Page<Book> getPopularBooks(Pageable nextPage);

    @Query(value = "SELECT * FROM books where DATE_TRUNC('month', pub_date) >= DATE_TRUNC('month', NOW() - INTERVAL '2 month') ORDER BY id",
            countQuery = "SELECT COUNT(*) FROM books where DATE_TRUNC('month', pub_date) >= DATE_TRUNC('month', NOW() - INTERVAL '2 month')",
            nativeQuery = true)
    Page<Book> getNewBooks(Pageable nextPage);

    @Query(value = "SELECT * FROM books where rating > 6 ORDER BY id",
            countQuery = "SELECT COUNT(*) FROM books where rating > 6",
            nativeQuery = true)
    Page<Book> getRecommendedBooks(Pageable nextPage);

    List<Book> findBooksBySlugIn(String[] slugs);

    Book findBookBySlug(String slug);

}
