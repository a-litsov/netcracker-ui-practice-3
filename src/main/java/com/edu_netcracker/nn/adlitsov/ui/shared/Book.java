package com.edu_netcracker.nn.adlitsov.ui.shared;

import java.util.Date;
import java.util.Objects;

public class Book {
    private int id;
    private String authorName, title;
    private int pagesCount;
    private Date addDate;

    public Book() {
    }

    public Book(int id, String authorName, String title, int pagesCount, Date addDate) {

        this.id = id;
        this.authorName = authorName;
        this.title = title;
        this.pagesCount = pagesCount;
        this.addDate = addDate;
    }

    public Book(String authorName, String title, int pagesCount) {

        this.authorName = authorName;
        this.title = title;
        this.pagesCount = pagesCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", authorName='" + authorName + '\'' +
                ", title='" + title + '\'' +
                ", pagesCount=" + pagesCount +
                ", addDate=" + addDate +
                '}';
    }
}
