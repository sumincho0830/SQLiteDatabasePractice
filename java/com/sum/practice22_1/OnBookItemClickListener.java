package com.sum.practice22_1;

import android.view.View;

public interface OnBookItemClickListener {
    public void onItemClick(BookListAdapter.ViewHolder holder, View view, int position); //홀더는 래퍼 클래스에 가깝다

}
