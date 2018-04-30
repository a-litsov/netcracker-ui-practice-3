package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.google.gwt.user.client.ui.SuggestOracle;

public class BookSuggestion implements SuggestOracle.Suggestion {
    private Book book;

    public BookSuggestion(Book book) {
        this.book = book;
    }

    @Override
    public String getDisplayString() {
        return book.toString();
    }

    @Override
    public String getReplacementString() {
        return book.toString();
    }
}
