package com.edu_netcracker.nn.adlitsov.ui.server;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.edu_netcracker.nn.adlitsov.ui.shared.MyColumnSortInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class BookStorage {
    private BookFileManager fileManager = new BookFileManager();
    private List<Book> books = new ArrayList<>();
    private int nextId = 1;

    {
        books = fileManager.loadBooks();
        nextId = getNextId(books);
    }


    public synchronized void addBook(Book book) {
        book.setId(nextId++);
        book.setAddDate(new Date());

        books.add(book);
        fileManager.saveBooks(books);
    }

    public synchronized void deleteBook(int id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                books.remove(i);
            }
        }
        fileManager.saveBooks(books);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> getBooks(int start, int length) {
        return books.subList(start, start + length);
    }

    public List<Book> getSortedBooks(int start, int length, List<MyColumnSortInfo> columnsSortInfo) {
        List<Book> sortedBooks = sort(columnsSortInfo);
        return sortedBooks.subList(start, start + length);
    }

    public int getBooksCount() {
        return books.size();
    }

    private Comparator<Book> appendComparator(Comparator<Book> sourceComp, MyColumnSortInfo sortInfo) {
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


    private int getNextId(List<Book> books) {
        int maxId = 0;
        for (Book book : books) {
            if (book.getId() > maxId) {
                maxId = book.getId();
            }
        }
        return maxId + 1;
    }
}
