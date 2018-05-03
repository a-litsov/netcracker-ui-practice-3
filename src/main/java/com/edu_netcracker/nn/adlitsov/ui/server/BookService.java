package com.edu_netcracker.nn.adlitsov.ui.server;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.edu_netcracker.nn.adlitsov.ui.shared.BooksQuery;
import com.edu_netcracker.nn.adlitsov.ui.shared.MyColumnSortInfo;
import com.edu_netcracker.nn.adlitsov.ui.shared.Pair;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
@Path("book")
public class BookService {
    private static final BookStorage bookStorage = new BookStorage();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void addBook(Book book) throws IllegalArgumentException {
        bookStorage.addBook(book);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getBooks() {
        return bookStorage.getBooks();
    }

    @GET
    @Path("search={query}&limit={limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> sortBooks(@PathParam("query") String query, @PathParam("limit") int limit) {
        return bookStorage.search(query, limit);
    }

    @DELETE
    public void deleteBooks(List<Integer> idList) {
        bookStorage.deleteBooks(idList);
    }

    @POST
    @Path("sortedRange")
    @Produces(MediaType.APPLICATION_JSON)
    public Pair<Integer, List<Book>> sortedRange(BooksQuery query) {
        int booksCount = bookStorage.getBooksCount();
        int start = query.getStart();
        int length = query.getLength() <= booksCount - start ? query.getLength() : booksCount - start;
        List<Book> subBooks;
        if (query.getSortInfo() != null)
            subBooks = bookStorage.getSortedBooks(start, length, new ArrayList<MyColumnSortInfo>() {{
                add(query.getSortInfo());
            }});
        else
            subBooks = bookStorage.getBooks(start, length);
        return new Pair<>(booksCount, subBooks);
    }
}
