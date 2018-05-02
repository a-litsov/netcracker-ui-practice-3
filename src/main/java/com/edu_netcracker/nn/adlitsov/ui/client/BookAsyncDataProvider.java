package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.edu_netcracker.nn.adlitsov.ui.shared.BooksQuery;
import com.edu_netcracker.nn.adlitsov.ui.shared.MyColumnSortInfo;
import com.edu_netcracker.nn.adlitsov.ui.shared.Pair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class BookAsyncDataProvider extends AsyncDataProvider<Book> {
    private CellTable<Book> table;
    private BookService bookService;

    public BookAsyncDataProvider(CellTable<Book> table, BookService bookService) {
        this.table = table;
        this.bookService = bookService;
    }

    @Override
    protected void onRangeChanged(HasData<Book> display) {
        int start = display.getVisibleRange().getStart();
        int length = display.getVisibleRange().getLength();

        // Get info about last column that requested sorting
        MyColumnSortInfo sortInfo = null;
        ColumnSortList sortList = table.getColumnSortList();
        if (sortList.size() > 0) {
            ColumnSortList.ColumnSortInfo columnSortInfo = table.getColumnSortList().get(0);
            sortInfo = new MyColumnSortInfo(columnSortInfo.getColumn().getDataStoreName(),
                                            columnSortInfo.isAscending());
        }
        BooksQuery query = new BooksQuery(sortInfo, start, length);

        bookService.sortedRange(query, new MethodCallback<Pair<Integer, List<Book>>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                GWT.log("Bad get books range request! :(");
            }

            @Override
            public void onSuccess(Method method, Pair<Integer, List<Book>> dataPair) {
                GWT.log("Got books range from server!");
                updateRowCount(dataPair.getFirst(), true);
                updateRowData(start, dataPair.getSecond());
            }
        });
    }
}
