package com.sum.practice22_1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnDatabaseCallback{
    private static final String TAG = "MainActivity";
    Toolbar toolbar;
    TabLayout tabs;

    InputFragment inputFragment;
    BookListFragment bookListFragment;

    BookDatabase database;
    boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Fragments
        inputFragment = new InputFragment();
        bookListFragment = new BookListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, inputFragment).commit();

        //TabLayout
        tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("입력"));
        tabs.addTab(tabs.newTab().setText("조회"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, inputFragment).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bookListFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Database
        if(database != null){ //열려 있는 데이터베이스가 있다면, 닫고 다시 열기
            database.close();//내부 데이터베이스를 닫고, 싱글턴 인스턴스가 null을 가리키도록 한다.
            database = null;

        }

        database = BookDatabase.getInstance(this);
        isOpen = database.open(); //내부에서 writableDatabase를 불러와서 db변수에 저장
        if(isOpen){
            println("BookDatabase is open.");
        }else{
            println("BookDatabase is not open.");
        }
    }

    @Override
    protected void onDestroy() { //액티비티가 사라지면 데이터베이스도 끄기
        if(database != null){
            database.close(); //내부에서는 db.close()를 실행하고 싱글턴 인스턴스가 null을 가리키도록 한다
            database = null;
        }
        super.onDestroy();
    }

    @Override
    public void insert(String title, String author, String contents) {
        //콜백 메서드
        database.insertRecord(title, author, contents);
        Toast.makeText(getApplicationContext(), "책 정보를 추가했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public ArrayList<Book> selectAll() {
        ArrayList<Book> result = database.selectAll();
        Toast.makeText(getApplicationContext(), "책 정보를 조회했습니다.", Toast.LENGTH_SHORT).show();
        return result;
    }

    @Override
    public void deleteRecord(int _id) {
        database.deleteRecord(_id);
    }

    public void println(String message){
        Log.d(TAG, message);
    }
}