package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.dto.SlugBookDTO;
import com.example.MyBookShopApp.entities.Book;
import com.example.MyBookShopApp.entities.BookReview;
import com.example.MyBookShopApp.entities.BookReviewLike;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;
    private final ResourceStorage storage;
    private final BookService bookService;

    public BookController(BookRepository bookRepository, ResourceStorage storage, BookService bookService) {
        this.bookRepository = bookRepository;
        this.storage = storage;
        this.bookService = bookService;
    }

    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String sessionId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }
        SlugBookDTO slugBookDTO = bookService.getSlugBook(slug, sessionId);
        model.addAttribute("slugBook", slugBookDTO);
        return "/books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@PathVariable("file") MultipartFile file,
                                   @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookRepository.findBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping("/rateBook/{slug}/{rate}")
    public String setRateBook(@PathVariable("slug") String slug,
                              @PathVariable("rate") Integer rate,
                              HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    bookService.setRateBook(rate, slug, sessionId);
                }
            }
        }
        return "redirect:/books/" + slug;
    }

    @PostMapping("/bookReview")
    public ResponseEntity<String> setBookReview(@RequestBody BookReview bookReview){
        bookService.setBookReview(bookReview);
        return ResponseEntity.ok("Отзыв успешно сохранен!");
    }

    @PostMapping("/book-review-like")
    public ResponseEntity<String> setBookReviewLike(@RequestBody BookReviewLike bookReviewLike){
        bookService.setBookReviewLike(bookReviewLike);
        return ResponseEntity.ok("Ваш лайк/дизлайк успешно сохранен!");
    }
}
