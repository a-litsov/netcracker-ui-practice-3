package com.edu_netcracker.nn.adlitsov.ui.shared;

public class MyColumnSortInfo {
    private String columnName;
    private boolean isAscending;

    public MyColumnSortInfo() {
    }

    public MyColumnSortInfo(String columnName, boolean isAscending) {
        this.columnName = columnName;
        this.isAscending = isAscending;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(boolean ascending) {
        isAscending = ascending;
    }
}
