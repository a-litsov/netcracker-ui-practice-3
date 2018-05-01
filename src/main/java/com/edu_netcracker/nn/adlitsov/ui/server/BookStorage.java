package com.edu_netcracker.nn.adlitsov.ui.server;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.edu_netcracker.nn.adlitsov.ui.shared.MyColumnSortInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class BookStorage {
    private List<Book> books = new ArrayList<>();
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

    public void deleteBook(int id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                books.remove(i);
            }
        }
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

    public Comparator<Book> appendComparator(Comparator<Book> sourceComp, MyColumnSortInfo sortInfo) {
        Comparator<Book> newComp = null;
        switch (sortInfo.getColumnName()) {
            case "id":
                newComp = Comparator.comparing(Book::getId);
                break;
            case "title":
                newComp = Comparator.comparing(Book::getTitle);
                break;
            case "author":
                newComp = Comparator.comparing(Book::getAuthorName);
                break;
            case "pages":
                newComp = Comparator.comparing(Book::getPagesCount);
                break;
            case "year":
                newComp = Comparator.comparing(Book::getYear);
                break;
            case "addDate":
                newComp = Comparator.comparing(Book::getAddDate);
                break;
            default:
                throw new IllegalArgumentException("Column-to-field mapping is not recognized! (Column " +
                                                           sortInfo.getColumnName() + " not found)");
        }
        if (!sortInfo.isAscending()) {
            newComp = newComp.reversed();
        }

        if (sourceComp != null) {
            return sourceComp.thenComparing(newComp);
        } else {
            return newComp;
        }
    }

    public List<Book> sort(List<MyColumnSortInfo> columnsSortInfo) {
        if (columnsSortInfo == null) {
            throw new IllegalArgumentException("Invalid sort info!");
        }

        Comparator<Book> comp = null;
        for (int i = columnsSortInfo.size() - 1; i >= 0; i--) {
            MyColumnSortInfo sortInfo = columnsSortInfo.get(i);
            comp = appendComparator(comp, sortInfo);
        }

        List<Book> sortBooks = new ArrayList<>(books);

        if (comp != null) {
            sortBooks.sort(comp);
        }

        return sortBooks;
    }

    public List<Book> search(String query, int limit) {
        List<Book> result = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < books.size() && k < limit; i++) {
            Book curBook = books.get(i);
            if (curBook.getTitle().toLowerCase().startsWith(query.toLowerCase())) {
                result.add(curBook);
                k++;
            }
        }
        return result;
    }
}
