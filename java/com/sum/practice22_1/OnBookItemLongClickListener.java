package com.sum.practice22_1;

import android.view.View;

public interface OnBookItemLongClickListener {
    public void onItemLongClick(BookListAdapter.ViewHolder holder, View view, int position, int itemID);
}
