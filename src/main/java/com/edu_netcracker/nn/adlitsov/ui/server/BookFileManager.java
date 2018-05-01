package com.edu_netcracker.nn.adlitsov.ui.server;

import com.edu_netcracker.nn.adlitsov.ui.shared.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BookFileManager {
    private static final String BOOKS_DIR_PATH = System.getProperty("java.io.tmpdir") + File.separator + "adlitsov-ui-gwt";
    private static final String BOOKS_FULL_PATH = BOOKS_DIR_PATH + File.separator + "books.json";

    private File createBooksFile() throws IOException {
        File dirFile = new File(BOOKS_DIR_PATH);
        if (!dirFile.exists()) {
            if (!dirFile.mkdir()) {
                System.err.println("Cannot create " + BOOKS_DIR_PATH + " folder!");
            }
        }
        File booksFile = new File(BOOKS_FULL_PATH);
        if (!booksFile.exists()) {
            if (!booksFile.createNewFile()) {
                System.err.println("Cannot create " + BOOKS_FULL_PATH + " folder!");
            }

        }
        return booksFile;
    }

    public void saveBooks(List<Book> books) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File booksFile = createBooksFile();
            mapper.writeValue(booksFile, books);
        } catch (IOException e) {
            System.err.println("Cannot save books to file!");
            e.printStackTrace();
        }
    }

    public List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        File booksFile = new File(BOOKS_FULL_PATH);
        if (!booksFile.exists()) {
            return loadDefaultBooks();
        }

        try {
            books = mapper.readValue(booksFile, new TypeReference<List<Book>>() {
            });
        } catch (IOException e) {
            System.err.println("Cannot load books!");
            books.clear();
            e.printStackTrace();
        }
        return books;
    }

    private List<Book> loadDefaultBooks() {
        List<Book> books = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("books.json");

        try {
            books = mapper.readValue(inputStream, new TypeReference<List<Book>>() {
            });
            inputStream.close();
        } catch (IOException e) {
            books.clear();
            System.err.println("Cannot load default books!");
            e.printStackTrace();
        }
        return books;
    }
}
