package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.edu_netcracker.nn.adlitsov.ui.shared.FieldVerifier;
import com.edu_netcracker.nn.adlitsov.ui.shared.MyColumnSortInfo;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

    static {
        // RestyGWT will use UNIX time and send it as JSONNumber
        Defaults.setDateFormat(null);
    }

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to Book the server. Please check your network "
            + "connection and try again.";

    /**
     * Create a remote service proxy to talk to the server-side Greeting service.
     */
    private final BookService bookService = GWT.create(BookService.class);

    private final Messages messages = GWT.create(Messages.class);

    private static final int ROWS_COUNT = 10;
    private static final int SUGGEST_LIMIT = 10;

    private CellTable<Book> table;
    private Book lastSelectedBook;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        String root = Defaults.getServiceRoot();
        root = root.replace("gwt/", "");
        Defaults.setServiceRoot(root);


        createAddPanel();
        createTable();
        loadAllBooksToTable();
        createRemovePanel();
    }

    private void createAddPanel() {
        final Label titleLabel = new Label("Название:");
        final TextBox titleField = new TextBox();
        titleField.setVisibleLength(25);

        final Label authorNameLabel = new Label("Автор:");
        final TextBox authorNameField = new TextBox();
        authorNameField.setVisibleLength(25);

        final Label pagesLabel = new Label("Страницы:");
        final IntegerBox pagesField = new IntegerBox();
//        pagesField.setStyleName("gwt-IntegerBox");
        pagesField.setMaxLength(4);
        pagesField.setVisibleLength(4);

        final Label yearLabel = new Label("Год:");
        final IntegerBox yearField = new IntegerBox();
//        yearField.setStyleName("gwt-IntegerBox");
        yearField.setMaxLength(4);
        yearField.setVisibleLength(4);

        final Button sendButton = new Button("Добавить!");

        final FlowPanel addBookPanel = new FlowPanel();
        addBookPanel.add(titledField(titleLabel, titleField));
        addBookPanel.add(titledField(authorNameLabel, authorNameField));
        addBookPanel.add(titledField(pagesLabel, pagesField));
        addBookPanel.add(titledField(yearLabel, yearField));

        addBookPanel.add(vertMidAlignedButton(sendButton));

        addBookPanel.setStyleName("book-add-panel");

        RootPanel.get("book-add-block").add(addBookPanel);

        // Create a handler for the sendButton and nameField
        class MyHandler implements ClickHandler {
            /**
             * Fired when the user clicks on the sendButton.
             */
            public void onClick(ClickEvent event) {
                sendBookToServer();
            }

            /**
             * Send the name from the nameField to the server and wait for a response.
             */
            private void sendBookToServer() {
                // First, we validate the input.
                String bookTitle = titleField.getText();
                String authorName = authorNameField.getText();
                int year = yearField.getValue();
                int pages = pagesField.getValue();
                if (!FieldVerifier.isValidName(bookTitle)) {
                    return;
                }
                System.out.println(year);

                Book book = new Book(bookTitle, authorName, pages, year);

                // Then, we send the input to the server.
//        sendButton.setEnabled(false);
                bookService.addBook(book, new MethodCallback<Void>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {
                        Label failureLabel = new Label("It's bad! :(");
                        RootPanel.get().add(failureLabel);
                    }

                    @Override
                    public void onSuccess(Method method, Void v) {
                        Label successLabel = new Label("Added book!");
                        RootPanel.get().add(successLabel);

                        loadAllBooksToTable();
                    }
                });
            }
        }

        // Add a handler to send the name to the server
        MyHandler handler = new MyHandler();
        sendButton.addClickHandler(handler);
    }

    public HorizontalPanel titledField(Label title, Widget field) {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel.setStyleName("titled-field");
        panel.add(title);
        panel.add(field);
        return panel;
    }

    public HorizontalPanel vertMidAlignedButton(Button button) {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        panel.setStyleName("button-panel");
        panel.add(button);
        return panel;
    }

    public void loadAllBooksToTable() {
        bookService.getBooks(new MethodCallback<List<Book>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Label failureLabel = new Label("It's bad! :(");
                RootPanel.get().add(failureLabel);
            }

            @Override
            public void onSuccess(Method method, List<Book> books) {
                Label successLabel = new Label("Got books!");
                RootPanel.get().add(successLabel);

                table.setRowData(books);
            }
        });

    }

    private void createTable() {
        // Create a CellTable.
        table = new CellTable<>();
        table.setStyleName("book-table");

        // Create id column.
        TextColumn<Book> idColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return Integer.toString(Book.getId());
            }
        };
        idColumn.setDataStoreName("id");
        idColumn.setSortable(true);

        // Create title column.
        TextColumn<Book> titleColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return Book.getTitle();
            }
        };
        titleColumn.setDataStoreName("title");

        // Make the title column sortable.
        titleColumn.setSortable(true);

        // Create author column.
        TextColumn<Book> authorColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return Book.getAuthorName();
            }
        };
        authorColumn.setDataStoreName("author");
        authorColumn.setSortable(true);

        // Create pages column.
        TextColumn<Book> pagesColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return Integer.toString(Book.getPagesCount());
            }
        };
        pagesColumn.setDataStoreName("pages");
        pagesColumn.setSortable(true);

        // Create year column.
        TextColumn<Book> yearColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return Integer.toString(Book.getYear());
            }
        };
        yearColumn.setDataStoreName("year");
        yearColumn.setSortable(true);

        // Create date column.
        TextColumn<Book> dateColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return DateTimeFormat.getFormat("dd-MM-yy").format(Book.getAddDate());
            }
        };
        dateColumn.setDataStoreName("addDate");
        dateColumn.setSortable(true);

        // Add the columns.
        table.addColumn(idColumn, "Id");
        table.addColumn(titleColumn, "Название");
        table.addColumn(authorColumn, "Автор");
        table.addColumn(pagesColumn, "Страницы");
        table.addColumn(yearColumn, "Год");
        table.addColumn(dateColumn, "Дата добавления");

//        ListDataProvider<Book> dataProvider = new ListDataProvider<Book>();
        table.setRowCount(ROWS_COUNT);

        table.addColumnSortHandler((event) -> {
            ColumnSortList sortList = table.getColumnSortList();
            List<MyColumnSortInfo> mySingleColumnSortList = new ArrayList<>();
            if (sortList != null && sortList.size() > 0) {
                Column<Book, ?> sortColumn = (Column<Book, ?>) sortList.get(0).getColumn();
                String name = sortColumn.getDataStoreName();
                boolean isAsc = event.isSortAscending();
                mySingleColumnSortList.add(new MyColumnSortInfo(name, isAsc));

                bookService.sortBooks(mySingleColumnSortList, new MethodCallback<List<Book>>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {
                        Label failureLabel = new Label("Bad sorting request! :(");
                        RootPanel.get().add(failureLabel);
                    }

                    @Override
                    public void onSuccess(Method method, List<Book> books) {
                        Label successLabel = new Label("Sorted books on server!");
                        RootPanel.get().add(successLabel);

                        table.setRowData(books);
                    }
                });
            }
        });

        // Add it to the root panel.
        RootPanel.get("book-table-block").add(table);
    }

    private SuggestBox createSuggestBox(Button deleteButton) {
        MultiWordSuggestOracle mwsOracle = new MultiWordsSuggestOracleREST(bookService, SUGGEST_LIMIT);
        SuggestBox suggestBox = new SuggestBox(mwsOracle);
        suggestBox.addSelectionHandler((selectionEvent) -> {
            BookSuggestion bookSuggestion = (BookSuggestion) selectionEvent.getSelectedItem();
            lastSelectedBook = bookSuggestion.getBook();
            deleteButton.setEnabled(true);
        });
        return suggestBox;
    }

    private void createRemovePanel() {
        FlowPanel removeBookPanel = new FlowPanel();
        removeBookPanel.setStyleName("book-remove-panel");

        Button deleteButton = new Button("Удалить");
        deleteButton.setStyleName("delete-button");
        deleteButton.addClickHandler(new DeleteButtonClickHandler(this, bookService, deleteButton));
        SuggestBox suggestBox = createSuggestBox(deleteButton);
        suggestBox.setStyleName("suggest-box");

        removeBookPanel.add(suggestBox);
        removeBookPanel.add(deleteButton);

        RootPanel.get("book-remove-block").add(removeBookPanel);
    }

    public Book getLastSelectedBook() {
        return lastSelectedBook;
    }
}
