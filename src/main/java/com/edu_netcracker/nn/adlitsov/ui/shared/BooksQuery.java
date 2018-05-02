package com.edu_netcracker.nn.adlitsov.ui.shared;

public class BooksQuery {
    private MyColumnSortInfo sortInfo;
    private int start;
    private int length;

    public BooksQuery() {
    }

    public BooksQuery(MyColumnSortInfo sortInfo, int start, int end) {
        this.sortInfo = sortInfo;
        this.start = start;
        this.length = end;
    }

    public MyColumnSortInfo getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(MyColumnSortInfo sortInfo) {
        this.sortInfo = sortInfo;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
