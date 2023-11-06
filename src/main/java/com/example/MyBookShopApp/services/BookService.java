package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.GenreDTO;
import com.example.MyBookShopApp.dto.SlugBookDTO;
import com.example.MyBookShopApp.dto.TagDTO;
import com.example.MyBookShopApp.entities.*;
import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import com.example.MyBookShopApp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private BookRepository bookRepository;
    private TagRepository tagRepository;
    private GenreRepository genreRepository;
    private RateBookRepository rateBookRepository;
    private BookReviewRepository bookReviewRepository;
    private BookReviewLikeRepository bookReviewLikeRepository;

    @Autowired
    public BookService(BookRepository bookRepository, TagRepository tagRepository, GenreRepository genreRepository, RateBookRepository rateBookRepository, BookReviewRepository bookReviewRepository, BookReviewLikeRepository bookReviewLikeRepository) {
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.genreRepository = genreRepository;
        this.rateBookRepository = rateBookRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
    }

    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorFirstNameContaining(authorName);
    }

    public List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        if (title.equals("") || title.length() <= 1) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<Book> data = bookRepository.findBooksByTitleContaining(title);
            if (data.size() > 0) {
                return data;
            } else {
                throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
            }
        }
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceOldBetween(min, max);
    }

    public List<Book> getBooksWithPrice(Integer price) {
        return bookRepository.findBooksByPriceOldIs(price);
    }

    public List<Book> getBooksWithMaxPrice() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageOfRecommendedBook(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getRecommendedBooks(nextPage);
    }

    public Page<Book> getNewBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getNewBooks(nextPage);
    }

    public Page<Book> getPageOfPopularBook(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getPopularBooks(nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, nextPage);
    }

    public Page<Book> getPageOfNewBookByDate(Date dateFrom, Date dateTo, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByPubDateIsBetweenOrderByPubDate(dateFrom, dateTo, nextPage);
    }

    public List<TagDTO> getAllTagsAndBookCount() {
        List<Tag> tags = tagRepository.findAll();
        List<TagDTO> tagBooksCount = new ArrayList<>();
        for (Tag tag : tags) {
            tagBooksCount.add(new TagDTO(tag.getName(), tag.getBookList().size()));
        }
        return tagBooksCount;
    }

    public List<GenreDTO> getGenresByParentIdIsNull() {

        List<Genre> genresParentIsNull = genreRepository.findGenresByParentIdIsNull();
        List<Integer> parentIdList = new ArrayList<>();
        for (Genre genre : genresParentIsNull) {
            parentIdList.add(genre.getId());
        }
        List<Genre> childGenres = genreRepository.findGenresByParentIdIn(parentIdList);

        List<GenreDTO> listGenres = new ArrayList<>();
        for (Genre genre : genresParentIsNull) {
            GenreDTO genreDTO = new GenreDTO();
            List<Genre> childs = childGenres.stream().filter(f -> genre.getId().equals(f.getParentId())).collect(Collectors.toList());
            genreDTO.setId(genre.getId());
            genreDTO.setSlug(genre.getSlug());
            genreDTO.setParentId(genre.getParentId());
            genreDTO.setName(genre.getName());
            genreDTO.setChild(childs);
            listGenres.add(genreDTO);
        }
        return listGenres;
    }

    public List<Genre> getGenresByParentId(Integer parentId) {
        List<Genre> genres = genreRepository.findGenresByParentId(parentId);
        return genres;
    }

    public void setRateBook(Integer rate, String slug, String userId) {
        Book book = bookRepository.findBookBySlug(slug);
        RateBook rateBook = rateBookRepository.getRateBook(book.getId(), userId);
        if (rateBook == null) {
            RateBook newRateBook = new RateBook();
            newRateBook.setBook(book);
            newRateBook.setUserId(userId);
            newRateBook.setRate(rate);
            rateBookRepository.save(newRateBook);
        } else {
            rateBook.setRate(rate);
            rateBookRepository.save(rateBook);
        }

    }
    public SlugBookDTO getSlugBook(String slug, String sessionId){
        SlugBookDTO slugBookDTO = new SlugBookDTO();
        Book book = bookRepository.findBookBySlug(slug);
        if (book != null) {
            List<RateBook> rateBook = rateBookRepository.findRateBookByBookId(book.getId());
            RateBook currentUser = rateBookRepository.getRateBook(book.getId(), sessionId);
            int currentUserRating = currentUser != null ? currentUser.getRate() : 0;
            int rateCount = rateBook.size();
            int ratingCount = rateBook.stream().mapToInt(RateBook::getRate).sum();
            int rate1=0, rate2=0, rate3=0, rate4=0, rate5=0;
            for (RateBook r : rateBook)
                switch (r.getRate()){
                    case 1: rate1++; break;
                    case 2: rate2++; break;
                    case 3: rate3++; break;
                    case 4: rate4++; break;
                    case 5: rate5++; break;
                    default: break;
                }
            int rating = 0;
            if(ratingCount > 0 && rateCount > 0) {
                rating = (int) Math.floor(ratingCount / rateCount);
            }
            slugBookDTO.setRate1(rate1);
            slugBookDTO.setRate2(rate2);
            slugBookDTO.setRate3(rate3);
            slugBookDTO.setRate4(rate4);
            slugBookDTO.setRate5(rate5);
            slugBookDTO.setBook(book);
            slugBookDTO.setRating(rating);
            slugBookDTO.setRateCount(rateCount);
            slugBookDTO.setCurrentUserRating(currentUserRating);
        }


        return slugBookDTO;
    }

    public void setBookReview(BookReview bookReview){
        BookReview bReveiw = bookReview;
        bookReviewRepository.save(bReveiw);

    }

    public void setBookReviewLike(BookReviewLike bookReviewLike){
        BookReviewLike bReviewLike = bookReviewLike;
        bookReviewLikeRepository.save(bReviewLike);
    }

}
