package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class DeleteButtonClickHandler implements ClickHandler {
    private Main main;
    private BookService bookService;
    private Button deleteButton;


    public DeleteButtonClickHandler(Main main, BookService bookService, Button deleteButton) {
        this.main = main;
        this.bookService = bookService;
        this.deleteButton = deleteButton;
    }


    @Override
    public void onClick(ClickEvent clickEvent) {
        Book lastSelectedBook = main.getLastSelectedBook();
        if (lastSelectedBook != null) {
            bookService.deleteBook(lastSelectedBook.getId(), new MethodCallback<Void>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    Label failureLabel = new Label("It's bad delete request! :(");
                    RootPanel.get().add(failureLabel);
                }

                @Override
                public void onSuccess(Method method, Void v) {
                    Label successLabel = new Label("Removed book!");
                    RootPanel.get().add(successLabel);

                    main.loadAllBooksToTable();
                    deleteButton.setEnabled(false);
                }
            });
        }
    }
}
