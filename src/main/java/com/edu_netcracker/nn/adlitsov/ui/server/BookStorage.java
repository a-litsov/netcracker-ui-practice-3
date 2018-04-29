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

    public List<Book> sortByColumn(String columnName, boolean isAsc) {
        Comparator<Book> comp = null;
        switch (columnName) {
            case "id":
                comp = Comparator.comparing(Book::getId);
                break;
            case "title":
                comp = Comparator.comparing(Book::getTitle);
                break;
            case "author":
                comp = Comparator.comparing(Book::getAuthorName);
                break;
            case "pages":
                comp = Comparator.comparing(Book::getPagesCount);
                break;
            case "year":
                comp = Comparator.comparing(Book::getYear);
                break;
            case "addDate":
                comp = Comparator.comparing(Book::getAddDate);
                break;
            default:
                throw new IllegalArgumentException("Column-to-field mapping is not recognized! (Column " +
                                                           columnName + " not found)");
        }
        if (!isAsc) {
            comp = comp.reversed();
        }

        List<Book> sortBooks = new ArrayList<>(books);
        sortBooks.sort(comp);

        return sortBooks;
    }

}
