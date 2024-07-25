package com.sum.practice22_1;

import java.util.ArrayList;

public interface OnDatabaseCallback {
    //각  프래그먼트에서 메인애티비티와 통신하기 위한 인터페이스
    // 각 프래그먼트는 매인액티비티를 멤버 필드로 가지고 있다.
    public void insert(String title, String author, String contents);
    public ArrayList<Book> selectAll();
    public void deleteRecord(int _id);
}
