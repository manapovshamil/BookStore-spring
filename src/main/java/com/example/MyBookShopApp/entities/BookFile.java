package com.example.MyBookShopApp.entities;

import com.example.MyBookShopApp.data.BookFileType;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "book_files")
public class BookFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String hash;

    @Column(name = "type_id")
    private Integer typeId;
    private String path;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public String getBookFileExtensionString(){
        return BookFileType.getExtensionStringByTypeID(typeId);
    }
}
