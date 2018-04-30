package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;

public class MultiWordsSuggestOracleREST extends MultiWordSuggestOracle {
    private BookService bookService;
    private int suggestLimit;

    MultiWordsSuggestOracleREST(BookService bookService, int suggestLimit) {
        this.bookService = bookService;
        this.suggestLimit = suggestLimit;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        String query = request.getQuery();
        bookService.search(query, suggestLimit, new MethodCallback<List<Book>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Label failureLabel = new Label("It's bad search! :(");
                RootPanel.get("gwtContainer").add(failureLabel);
            }

            @Override
            public void onSuccess(Method method, List<Book> books) {
                Label successLabel = new Label("Got suggestions!");
                RootPanel.get("gwtContainer").add(successLabel);


                Response resp = new Response();
                List<SuggestOracle.Suggestion> suggestions = new ArrayList<>();
                for (Book book : books) {
                    suggestions.add(new BookSuggestion(book));
                }
                resp.setSuggestions(suggestions);

                callback.onSuggestionsReady(request, resp);
            }
        });
    }
}
