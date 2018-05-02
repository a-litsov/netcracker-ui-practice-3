package com.edu_netcracker.nn.adlitsov.ui.client;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.Range;

public class FixedRangesSimplePager extends SimplePager {
    @Override
    public void setPageStart(int index) {
        if (getDisplay() != null) {
            Range range = getDisplay().getVisibleRange();
            int pageSize = range.getLength();

            // Removed the min to show fixed ranges (last page issue workaround)
            //if (isRangeLimited && display.isRowCountExact()) {
            //  index = Math.min(index, display.getRowCount() - pageSize);
            //}

            index = Math.max(0, index);
            if (index != range.getStart()) {
                getDisplay().setVisibleRange(index, pageSize);
            }
        }
    }
}
