package com.example.MyBookShopApp.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"author", "tag"})
@ToString(exclude = {"author", "tag"})
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pub_date")
    private Date pubDate;

    @Column(name = "is_bestseller")
    private Boolean isBesteller;
    private String slug;
    private String title;
    private String image;
    @Column(length = 2000)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private List<BookFile> bookFileList;

    @Column(name = "price")
    @JsonProperty("price")
    private Integer priceOld;

    @Column(name = "discount")
    @JsonProperty("discount")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonIgnore
    private Author author;

    @Column(name = "number_of_users_bought_book")
    private Integer numberOfUsersBoughtBook;

    @Column(name = "number_of_users_basket_book")
    private Integer numberOfUsersBasketBook;

    @Column(name = "number_of_users_postponed_book")
    private Integer numberOfUsersPostponedBook;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    @JsonIgnore
    private Tag tag;
    private Integer rating;
    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @JsonIgnore
    private RateBook rateBook;

    @JsonGetter()
    public Integer discountPrice(){
        Integer discountedPrice = priceOld - Math.toIntExact(Math.round(price*priceOld));
        return discountedPrice;
    }
    @JsonGetter("authors")
    public String authorsFullName (){
        return author.getLastName() + " " + author.getFirstName();
    }
}
