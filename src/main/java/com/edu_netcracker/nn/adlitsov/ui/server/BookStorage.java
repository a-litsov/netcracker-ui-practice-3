package com.edu_netcracker.nn.adlitsov.ui.server;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BookStorage {
    private Set<Book> books = new HashSet<>();
    private int lastId = 0;

    public void addBook(Book book) {
        book.setId(lastId++);
        book.setAddDate(new Date());

        books.add(book);
    }

    public Book[] getBooks() {
        return books.toArray(new Book[books.size()]);
    }

}
