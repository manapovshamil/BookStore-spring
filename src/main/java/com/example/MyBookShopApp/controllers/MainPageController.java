package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.BooksPageDto;
import com.example.MyBookShopApp.dto.GenreDTO;
import com.example.MyBookShopApp.dto.GenresPageDTO;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class MainPageController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public MainPageController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }


    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("recommendedBooks", bookService.getPageOfRecommendedBook(0, 15).getContent());
        model.addAttribute("tags", bookService.getAllTagsAndBookCount());
        return "index";
    }

    @GetMapping("/genres")
    public String genresPage(Model model) {
        model.addAttribute("genresByParentIdIsNull", bookService.getGenresByParentIdIsNull());
        return "genres/index";
    }

    @GetMapping("/recent")
    public String newBooksPage(Model model) {
        model.addAttribute("newBooks", bookService.getNewBooks(0, 5).getContent());
        return "books/recent";
    }

    @GetMapping("/popular")
    public String popularBooksPage(Model model) {
        model.addAttribute("popularBooks", bookService.getPageOfPopularBook(0, 10).getContent());
        return "books/popular";
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("getAuthorsData", authorService.getAuthorsData());
        return "/authors/index";
    }

    @GetMapping("books/recommended")
    @ResponseBody
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBook(offset, limit).getContent());
    }

    @GetMapping("/tags")
    public String tagsPage(Model model) {
        return "tags/index";
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        if(searchWordDto!=null){

            model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("searchResults",
                bookService.getPageOfSearchResultBooks(searchWordDto.getText(), 0, 5).getContent());
        return "/search/index";
        }else {
            throw new EmptySearchException("Поиск по null невозможен");
        }

}

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getText(), offset, limit).getContent());
    }

    @GetMapping("books/recent")
    @ResponseBody
    public BooksPageDto newBooksByDatePage(@RequestParam("from") @DateTimeFormat(pattern = "dd.MM.yyyy") Date DateFrom,
                                           @RequestParam("to") @DateTimeFormat(pattern = "dd.MM.yyyy") Date DateTo,
                                           @RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfNewBookByDate(DateFrom, DateTo, offset, limit).getContent());
    }

    @GetMapping("books/popular")
    @ResponseBody
    public BooksPageDto popularBooksPage(@RequestParam("offset") Integer offset,
                                         @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBook(offset, limit).getContent());
    }

    @GetMapping("/list-genres")
    public ResponseEntity<List<GenreDTO>> getGenresByParentIdIsNull() {
        return ResponseEntity.ok(bookService.getGenresByParentIdIsNull());
    }

    @GetMapping("/by-parent-genres")
    public ResponseEntity<GenresPageDTO> getGenresByParentId(@RequestParam("parentId") Integer parentId) {
        return ResponseEntity.ok( new GenresPageDTO(bookService.getGenresByParentId(parentId)));
    }

    @GetMapping("books/author-page/{authorId}")
    public String getAuthorPage(@PathVariable(value = "authorId", required = false) Integer authorId,
                                Model model) {
        model.addAttribute("authorInfo", authorService.getAuthorInfo(authorId));
        return "/authors/slug";
    }

}
