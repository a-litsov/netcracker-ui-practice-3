package com.edu_netcracker.nn.adlitsov.ui.server;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;

import java.util.*;

public class BookStorage {
    private Set<Book> books = new TreeSet<>(Comparator.comparing(Book::getId));
    private int lastId = 0;

    {
        // add sample books
        addBook(new Book("Code Complete", "Steve McConnell", 914, 1993));
        addBook(new Book("The Art of Computer Programming", "Donald Knuth", 3168, 1988));
        addBook(new Book("Clean Code", "Robert Cecil Martin", 464, 2008));
        addBook(new Book("Effective Java", "Joshua Bloch", 416, 2018));
        addBook(new Book("Cracking the Coding Interview: 189 Programming Questions and Solutions",
                         "Gayle Laakmann McDowell", 508, 2011));
        addBook(new Book("Algorithms + Data Structures = Programs", "Niklaus Wirth", 366, 1976));
    }

    public void addBook(Book book) {
        book.setId(lastId++);
        book.setAddDate(new Date());

        books.add(book);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

}
