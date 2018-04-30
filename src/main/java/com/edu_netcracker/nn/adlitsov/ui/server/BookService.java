package com.edu_netcracker.nn.adlitsov.ui.server;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    @Path("sort={columnName}&isAsc={asc}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> sortBooks(@PathParam("columnName") String columnName, @PathParam("asc") boolean asc) {
        return bookStorage.sortByColumn(columnName, asc);
    }

    @GET
    @Path("search={query}&limit={limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> sortBooks(@PathParam("query") String query, @PathParam("limit") int limit) {
        return bookStorage.search(query, limit);
    }
}
