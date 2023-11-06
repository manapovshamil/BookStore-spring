package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.dto.SlugBookDTO;
import com.example.MyBookShopApp.entities.Book;
import com.example.MyBookShopApp.entities.BookFile;
import com.example.MyBookShopApp.entities.RateBook;
import com.example.MyBookShopApp.entities.TestEntity;
import com.example.MyBookShopApp.repositories.BookFileRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.RateBookRepository;
import com.example.MyBookShopApp.repositories.TestEntityCrudRepository;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.logging.Logger;

@Configuration
public class CommandLineRunnerImpl implements CommandLineRunner {

    TestEntityCrudRepository testEntityCrudRepository;
    BookRepository bookRepository;
    BookFileRepository bookFileRepository;
    RateBookRepository rateBookRepository;
    BookService bookService;

    @Autowired
    public CommandLineRunnerImpl(TestEntityCrudRepository testEntityCrudRepository, BookRepository bookRepository, RateBookRepository rateBookRepository, BookService bookService) {
        this.testEntityCrudRepository = testEntityCrudRepository;
        this.bookRepository = bookRepository;
        this.rateBookRepository = rateBookRepository;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
//        for (int i = 0; i < 5; i++) {
//            createTestEntity(new TestEntity());
//        }
//
//        TestEntity readTestEntity = readTestEntityById(3L);
//        if (readTestEntity != null) {
//            Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info("read " + readTestEntity.toString());
//        } else {
//            throw new NullPointerException();
//        }
//
//        TestEntity updateTestEntity = updateTestEntityById(5L);
//        if (updateTestEntity != null) {
//            Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info("update " + readTestEntity.toString());
//        } else {
//            throw new NullPointerException();
//        }
//
//        deleteTestEntityById(4L);
//
//        Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info(bookRepository.findBooksByAuthor_FirstName("Jelene Hopfner").toString());
//        Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info(bookRepository.customFindAllBooks().toString());

    }

    private void deleteTestEntityById(Long id) {
        TestEntity testEntity = testEntityCrudRepository.findById(id).get();
        testEntityCrudRepository.delete(testEntity);
    }

    private TestEntity updateTestEntityById(Long id) {
        TestEntity testEntity = testEntityCrudRepository.findById(id).get();
        testEntity.setData("NEW DATA");
        testEntityCrudRepository.save(testEntity);
        return testEntity;
    }


    private TestEntity readTestEntityById(Long id) {
        return testEntityCrudRepository.findById(id).get();
    }


    private void createTestEntity(TestEntity entity) {
        entity.setData(entity.getClass().getSimpleName() + entity.hashCode());
        testEntityCrudRepository.save(entity);
    }
}
