package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.view.client.SetSelectionModel;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteSelectedButtonClickHandler implements ClickHandler {
    private BookService bookService;
    private CellTable<Book> table;
    private SetSelectionModel<Book> selectionModel;

    public DeleteSelectedButtonClickHandler() {

    }

    public DeleteSelectedButtonClickHandler(BookService bookService, CellTable<Book> table) {
        this.bookService = bookService;
        this.table = table;
        this.selectionModel = (SetSelectionModel<Book>) table.getSelectionModel();
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        List<Integer> idList = getSelectedIdList();
        if (!idList.isEmpty()) {
            bookService.deleteBooks(idList, new MethodCallback<Void>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    GWT.log("It's bad delete request! :(");
                }

                @Override
                public void onSuccess(Method method, Void v) {
                    GWT.log("Removed book(s)!");

                    table.setRowCount(table.getRowCount() - 1);
                    table.setVisibleRangeAndClearData(table.getVisibleRange(), true);
                    selectionModel.clear();
                }
            });
        }
    }

    private List<Integer> getSelectedIdList() {
        return selectionModel.getSelectedSet().stream().map(Book::getId).collect(Collectors.toList());
    }
}
