package com.edu_netcracker.nn.adlitsov.ui.client;

public class Validators {
    public static final int CURRENT_YEAR = 2018;

    public static boolean title(String title) {
        return title.toLowerCase().matches("^(?:[a-zа-яё0-9]+(?: |\\.\\.?\\.? ?|, ?|! ?|\\? ?| ?\\( ?| ?\\) ?| ?; " +
                                                   "?| ?: ?| ?- ?| ?\\+ ?| ?= ?)?)+[a-zа-яё0-9.?!;)]$");
    }

    public static boolean author(String author) {
        return author.toLowerCase().matches("^(?:[a-zа-я]+(?: |\\\\. ?)?)+[a-zа-я]$");
    }

    public static boolean pages(String pages) {
        return pages.matches("^[1-9]\\d+");
    }

    public static boolean year(String year) {
        return year.matches("^[1-9]\\d+") && Integer.parseInt(year) <= CURRENT_YEAR;
    }
}
