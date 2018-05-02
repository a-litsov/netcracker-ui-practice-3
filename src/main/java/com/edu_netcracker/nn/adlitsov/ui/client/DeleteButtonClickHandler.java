package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
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
                    GWT.log("It's bad delete request! :(");
                }

                @Override
                public void onSuccess(Method method, Void v) {
                    GWT.log("Removed book!");

                    CellTable<Book> table = main.getTable();
                    table.setRowCount(table.getRowCount() - 1);
                    table.setVisibleRangeAndClearData(table.getVisibleRange(), true);
                    deleteButton.setEnabled(false);
                }
            });
        }
    }
}
