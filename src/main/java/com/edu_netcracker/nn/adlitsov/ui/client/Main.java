package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.AsyncDataProvider;
import org.fusesource.restygwt.client.Defaults;

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

    private static final int VISIBLE_ROWS_COUNT = 10;
    private static final int SUGGEST_LIMIT = 5;

    private CellTable<Book> table;

    private Book lastSelectedBook;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        String root = Defaults.getServiceRoot();
        root = root.replace("gwt/", "");
        Defaults.setServiceRoot(root);

        table = createTable();

        createAddPanel();
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
        pagesField.setMaxLength(4);
        pagesField.setVisibleLength(4);

        final Label yearLabel = new Label("Год:");
        final IntegerBox yearField = new IntegerBox();
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

        // Add a handler to send the book to the server
        ClickHandler handler = new AddButtonClickHandler(table, bookService, titleField, authorNameField,
                                                         yearField, pagesField);
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

    private CellTable<Book> createTable() {
        // Create a CellTable.
        CellTable<Book> table = new CellTable<>();
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


        table.addColumnSortHandler((event) -> {
            table.setVisibleRangeAndClearData(table.getVisibleRange(), true);
        });

        table.setPageSize(VISIBLE_ROWS_COUNT);
        AsyncDataProvider<Book> provider = new BookAsyncDataProvider(table, bookService);
        provider.addDataDisplay(table);

        SimplePager pager = new FixedRangesSimplePager();
        pager.setDisplay(table);


        // Add them to the panel
        RootPanel.get("book-table-block").add(table);
        RootPanel.get("book-table-block").add(pager);

        return table;
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
        deleteButton.setEnabled(false);
        deleteButton.setStyleName("delete-button", true);
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

    public CellTable<Book> getTable() {
        return table;
    }
}
