package com.edu_netcracker.nn.adlitsov.ui.client;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.edu_netcracker.nn.adlitsov.ui.shared.BooksQuery;
import com.edu_netcracker.nn.adlitsov.ui.shared.Pair;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface BookService extends RestService {

    @POST
    @Path("api/book")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    void addBook(Book book,
                 MethodCallback<Void> callback);

    @GET
    @Path("api/book")
    @Produces(MediaType.APPLICATION_JSON)
    void getBooks(MethodCallback<List<Book>> callback);

    @GET
    @Path("api/book/search={query}&limit={limit}")
    @Produces(MediaType.APPLICATION_JSON)
    void search(@PathParam("query") String query, @PathParam("limit") int limit, MethodCallback<List<Book>> callback);

    @DELETE
    @Path("api/book/")
    void deleteBook(Integer id, MethodCallback<Void> callback);

    @POST
    @Path("api/book/sortedRange")
    @Produces(MediaType.APPLICATION_JSON)
    void sortedRange(BooksQuery query,
                     MethodCallback<Pair<Integer, List<Book>>> callback);
}
