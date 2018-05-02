package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class AddButtonClickHandler implements ClickHandler {
    private CellTable<Book> table;
    private BookService bookService;
    private TextBox titleField, authorNameField;
    private IntegerBox yearField, pagesField;

    public AddButtonClickHandler(CellTable<Book> table, BookService bookService, TextBox titleField,
                                 TextBox authorNameField, IntegerBox yearField, IntegerBox pagesField) {
        this.table = table;
        this.bookService = bookService;
        this.titleField = titleField;
        this.authorNameField = authorNameField;
        this.yearField = yearField;
        this.pagesField = pagesField;
    }

    public void onClick(ClickEvent event) {
        // First, we validate the input.
        String bookTitle = titleField.getText();
        String authorName = authorNameField.getText();

        if (!Validators.title(bookTitle) || !Validators.author(authorName) || !Validators.pages(pagesField.getText())
                || !Validators.year(yearField.getText())) {
            PopupPanel warningPanel = new PopupPanel(true);
            warningPanel.setWidget(new Label("Данные книги введены некорректно!"));
            warningPanel.show();
            return;
        }

        int year = yearField.getValue();
        int pages = pagesField.getValue();

        Book book = new Book(bookTitle, authorName, pages, year);

        bookService.addBook(book, new MethodCallback<Void>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                GWT.log("It's bad adding book try! :(");
            }

            @Override
            public void onSuccess(Method method, Void v) {
                GWT.log("Added book!");
                table.setRowCount(table.getRowCount() + 1);
                table.setVisibleRangeAndClearData(table.getVisibleRange(), true);
            }
        });
    }
}