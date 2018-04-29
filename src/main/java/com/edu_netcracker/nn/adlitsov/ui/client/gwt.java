package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.edu_netcracker.nn.adlitsov.ui.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwt implements EntryPoint {

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

    private CellTable<Book> table;

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
    }

    private void createAddPanel() {
        final Label titleLabel = new Label("Title:");
        final TextBox titleField = new TextBox();

        final Label authorNameLabel = new Label("Author:");
        final TextBox authorNameField = new TextBox();

        final Label pagesLabel = new Label("Pages:");
        final IntegerBox pagesField = new IntegerBox();

        final Label yearLabel = new Label("Year:");
        final IntegerBox yearField = new IntegerBox();

        final Button sendButton = new Button("Submit");

        final HorizontalPanel addBookPanel = new HorizontalPanel();
        addBookPanel.add(titleLabel);
        addBookPanel.add(titleField);
        addBookPanel.add(authorNameLabel);
        addBookPanel.add(authorNameField);
        addBookPanel.add(pagesLabel);
        addBookPanel.add(pagesField);
        addBookPanel.add(yearLabel);
        addBookPanel.add(yearField);
        addBookPanel.add(sendButton);

        RootPanel.get("gwtContainer").add(addBookPanel);

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
                        RootPanel.get("gwtContainer").add(failureLabel);
                    }

                    @Override
                    public void onSuccess(Method method, Void v) {
                        Label successLabel = new Label("Added book!");
                        RootPanel.get("gwtContainer").add(successLabel);

                        loadAllBooksToTable();
                    }
                });
            }
        }

        // Add a handler to send the name to the server
        MyHandler handler = new MyHandler();
        sendButton.addClickHandler(handler);
    }

    private void loadAllBooksToTable() {
        bookService.getBooks(new MethodCallback<List<Book>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Label failureLabel = new Label("It's bad! :(");
                RootPanel.get("gwtContainer").add(failureLabel);
            }

            @Override
            public void onSuccess(Method method, List<Book> books) {
                Label successLabel = new Label("Got books!");
                RootPanel.get("gwtContainer").add(successLabel);

                table.setRowData(books);
            }
        });

    }

    private void createTable() {
        // Create a CellTable.
        table = new CellTable<>();

        // Create title column.
        TextColumn<Book> titleColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return Book.getTitle();
            }
        };

        // Make the title column sortable.
        titleColumn.setSortable(true);

        // Create author column.
        TextColumn<Book> authorColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return Book.getAuthorName();
            }
        };

        // Create pages column.
        TextColumn<Book> pagesColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return Integer.toString(Book.getPagesCount());
            }
        };

        // Create address column.
        TextColumn<Book> yearColumn = new TextColumn<Book>() {
            @Override
            public String getValue(Book Book) {
                return Integer.toString(Book.getYear());
            }
        };


        // Add the columns.
        table.addColumn(titleColumn, "Title");
        table.addColumn(authorColumn, "Author");
        table.addColumn(pagesColumn, "Pages count");
        table.addColumn(yearColumn, "Year");

        // Create a data provider.
//        ListDataProvider<Book> dataProvider = new ListDataProvider<Book>();
        table.setRowCount(ROWS_COUNT);

        // Add it to the root panel.
        RootPanel.get("gwtContainer").add(table);
    }
}
