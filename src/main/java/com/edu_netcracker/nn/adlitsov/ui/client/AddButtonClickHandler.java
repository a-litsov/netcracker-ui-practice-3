package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AddButtonClickHandler implements ClickHandler {
    private CellTable<Book> table;
    private BookService bookService;
    private TextBox titleField, authorNameField;
    private IntegerBox yearField, pagesField;
    private Label warningLabel;


    public AddButtonClickHandler(CellTable<Book> table, BookService bookService, TextBox titleField,
                                 TextBox authorNameField, IntegerBox yearField, IntegerBox pagesField) {
        this.table = table;
        this.bookService = bookService;
        this.titleField = titleField;
        this.authorNameField = authorNameField;
        this.yearField = yearField;
        this.pagesField = pagesField;

        warningLabel = new Label("Данные книги введены неверно! Книга не добавлена.");
        warningLabel.addStyleName("book-add-warning-label");
    }

    public void onClick(ClickEvent event) {
        // First, we validate the input.
        if (!validateInputAndWarn()) {
            return;
        }
        String bookTitle = titleField.getText();
        String authorName = authorNameField.getText();
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

    private boolean validateInputAndWarn() {
        Map<ValueBoxBase<?>, Function<String, Boolean>> widgetValidatorMap = new HashMap<>();
        widgetValidatorMap.put(titleField, Validators::title);
        widgetValidatorMap.put(authorNameField, Validators::author);
        widgetValidatorMap.put(yearField, Validators::year);
        widgetValidatorMap.put(pagesField, Validators::pages);

        boolean isCorrect = true;
        for (Map.Entry<ValueBoxBase<?>, Function<String, Boolean>> entry : widgetValidatorMap.entrySet()) {
            if (!entry.getValue().apply(entry.getKey().getText())) {
                entry.getKey().addStyleDependentName("invalidEntry");
                isCorrect = false;
            } else {
                entry.getKey().removeStyleDependentName("invalidEntry");
            }
        }

        if (!isCorrect) {
            RootPanel.get("book-add-block").add(warningLabel);
        } else {
            RootPanel.get("book-add-block").remove(warningLabel);
        }

        return isCorrect;
    }
}