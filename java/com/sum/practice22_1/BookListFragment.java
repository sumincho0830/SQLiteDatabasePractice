package com.sum.practice22_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookListFragment extends Fragment {
    RecyclerView recyclerView;
    BookListAdapter adapter;

    OnDatabaseCallback callback; //얘는 뭐지?
    //영화 받아쓰기처럼, 몰라도 일단 계속계속 많이많이 하다보면 높은 수준에 오르게 된다. 언젠간 알겠지 ㅅㅂ

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (OnDatabaseCallback) getActivity(); //이 프래그먼트가 부착된 액티비틴
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_booklist, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BookListAdapter();
        recyclerView.setAdapter(adapter);

        ArrayList<Book> result = callback.selectAll(); //MainActivity에 있는 데이터베이스에서 가져오기 -> 액티비티에 selectAll()을 구현하여 프래그먼트로 전달
        adapter.setItems(result);
        //처음에는 아무렇게나 막 하지만, 그게 몇 년이 되면 겉잡을 수 없어진다.

        adapter.setOnItemClickListener(new OnBookItemClickListener() {
            @Override
            public void onItemClick(BookListAdapter.ViewHolder holder, View view, int position) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setIcon(R.drawable.ic_journal);
                dlg.setTitle("Book Contents");
                dlg.setMessage(adapter.getItem(position).getContent());
                dlg.setPositiveButton("닫기", null);
                dlg.show();
            }
        });

        adapter.setOnItemLongClickListener(new OnBookItemLongClickListener() {
            @Override
            public void onItemLongClick(BookListAdapter.ViewHolder holder, View view, int position, int itemID) {
                //다이얼로그로 보여주기
                // 삭제 버튼 누르면
                // - database에서 지우고, 새로고침

                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setIcon(R.drawable.delete);
                dlg.setTitle("Delete Record?");
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.deleteRecord(itemID);
                        ArrayList<Book> result = callback.selectAll();
                        adapter.setItems(result);
                    }
                });
                dlg.setNegativeButton("아니오", null);
                dlg.show();
            }
        });

        Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Book> result = callback.selectAll();
                adapter.setItems(result);
            }
        });

        //Fragment에 listener을 설정해서 데이터베이스에 변화가 생기면 프래그먼트가 작동하도록 할 수 있는가? -> 한 번 바꿔보자.
        return rootView;
    }
}
