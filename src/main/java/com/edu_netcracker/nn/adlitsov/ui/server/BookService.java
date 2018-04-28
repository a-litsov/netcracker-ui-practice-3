package com.edu_netcracker.nn.adlitsov.ui.server;

import com.edu_netcracker.nn.adlitsov.ui.server.BookStorage;
import com.edu_netcracker.nn.adlitsov.ui.shared.Book;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * The server side implementation of the RPC service.
 */
@Path("book")
public class BookService {
    private BookStorage bookStorage = new BookStorage();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Book addBook(Book book) throws IllegalArgumentException {
        bookStorage.addBook(book);

        return book;
    }
}
