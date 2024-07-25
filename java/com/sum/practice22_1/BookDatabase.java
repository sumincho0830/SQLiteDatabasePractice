package com.sum.practice22_1;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookDatabase {
    /**
     * TAG for debugging
     */
    public static final String TAG = "BookDatabase";
    /**
     * Singleton instace for BookDatabase
     */
    private static BookDatabase database;

    /**
     * Database Name
     */
    public static final String DATABASE_NAME = "library.db";
    /**
     * Database Version
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * Table Name for BookInfo
     */
    public static String TABLE_BOOK_INFO = "book_info";

    /**
     * Helper Class Defined
     */
    private DatabaseHelper dbHelper;
    /**
     * Database Object
     */
    private SQLiteDatabase db;

    Context context;

    private BookDatabase(Context context){
        this.context = context;
    }

    public static BookDatabase getInstance(Context context){
        // 싱글턴 인스턴스이므로, 여러개가 생성되면 안된다. 따라서 인스턴스가 생성되지 않은 경우(데이터베이스가 닫혀 있었던 경우)에만 새로운 데이터베이스를 생성하도록 한다.
       if(database == null){
           database = new BookDatabase(context);
       }

        return database;
    }

    public boolean open(){
        println("Opening Database ["+DATABASE_NAME+"].");

        dbHelper = new DatabaseHelper(context); //BookDatabase 인스턴스에 저장된 context
        db = dbHelper.getWritableDatabase(); //SQLiteDatabase 변수에 실제 데이터베이스 불러와서 저장하기

        if(db != null){
            return true;
        }else{
            return false;
        }
    }

    public void close(){
        println("Closing Database ["+DATABASE_NAME+"].");
        if(database != null){
            db.close();
            database = null; //데이터베이스를 닫고, BookDatabase 싱글턴 객체가 null을 가리키도록 한다.
        }
    }

    public Cursor rawQuery(String SQL){
        println("\nExecute rawQuery Called.\n");
        Cursor cursor = null;
        try{
            cursor = db.rawQuery(SQL, null);
        }catch (Exception e){
            Log.e(TAG, "Exception in rawQuery: ",e);
        }
        return cursor;
    }

    public boolean execSQL(String SQL){
        println("\n ExecSQL Called \n");

        try{
            println("SQL: "+SQL);
            db.execSQL(SQL); //Sql 실행에는 예외 처리가 필요하다.
            //리턴문은 여기 있어도 된다.
        }catch (Exception e){
            Log.e(TAG, "Exception in execSQL: ",e );
            return false;
        }
        return true;
    }

    public void insertRecord(String title, String author, String contents){
        //왜 여기에서 dbHelper의 내부 insertRecord() 메서드를 호출하지 않지?
        try{
            db.execSQL("INSERT INTO "+TABLE_BOOK_INFO+" (title, author, contents) VALUES ('" +title + "', '" + author + "', '" + contents + "')");
            println("new Record inserted.");
        }catch (Exception e){
            Log.e(TAG, "Exception in insertRecord: ",e);
        }
    }

    public ArrayList<Book> selectAll(){
        ArrayList<Book> books = new ArrayList<>();
        try{
            Cursor cursor = db.rawQuery("SELECT _id, title, author, contents FROM "+TABLE_BOOK_INFO, null);
            cursor.moveToFirst();
            do{ //저장할 때 id도 같이 저장
                int _id = cursor.getInt(0);
                String title = cursor.getString(1);
                String author = cursor.getString(2);
                String contents = cursor.getString(3);
                Book book = new Book(_id, title, author, contents);
                books.add(book);
            }while(cursor.moveToNext());
            cursor.close();

        }catch (Exception e){
            Log.e(TAG, "Exception in selectAll: ",e);
        }

        return books;
    }

    public void deleteRecord(int _id){
        try{
            db.execSQL("DELETE FROM "+TABLE_BOOK_INFO+" WHERE _id = "+_id);
            println("Record deleted.");
        }catch (Exception e){
            Log.e(TAG, "Exception in deleteRecord: ",e);
        }
    }


    public void println(String message){
        Log.d(TAG, message);
    }


    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }


        // 먼저 _db가 생성되고, getWritableDatabase() 등의 메서드를 통해 전역변수 db에 저장된다.
        @Override
        public void onCreate(SQLiteDatabase _db) { //SQLiteOpenHelper을 통해 생성된 데이터베이스에 데이터 추가
            /** 테이블 생성하기 **/
            println("Creating Table ["+TABLE_BOOK_INFO+"].");

            String DROP_SQL = "DROP TABLE IF EXISTS "+TABLE_BOOK_INFO;
            try{
                _db.execSQL(DROP_SQL);
            }catch (Exception e){
                Log.e(TAG, "Exception in DROP_SQL",e);
            }

            String CREATE_SQL = "CREATE TABLE "+TABLE_BOOK_INFO+" (" +
                                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    "title TEXT,"+
                                    "author TEXT,"+
                                    "contents TEXT,"+
                                    "create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                                ");";

            try{
                _db.execSQL(CREATE_SQL);
            }catch (Exception e){
                Log.e(TAG, "Exception in CREATE_SQL",e);
            }

            //Insert book records
            insertRecord(_db, "Do it! 안드로이드 앱 프로그래밍", "정재곤", "안드로이드 기본서로 이지스퍼블리싱 출판사에서 출판했습니다.");
            insertRecord(_db, "Programming Android", "Mednieks, Zigurd", "Oreilly Associates Inc에서 2011년 04월에 출판했습니다.");
            insertRecord(_db, "센차터치 모바일 프로그래밍", "이병옥,최성민 공저", "에이콘출판사에서 2011년 10월에 출판했습니다.");
            insertRecord(_db, "시작하세요! 안드로이드 게임 프로그래밍", "마리오 제흐너 저", "위키북스에서 2011년 09월에 출판했습니다.");
            insertRecord(_db, "실전! 안드로이드 시스템 프로그래밍 완전정복", "박선호,오영환 공저", "DW Wave에서 2010년 10월에 출판했습니다.");

        }

        @Override
        public void onOpen(SQLiteDatabase _db) {
            println("Opened Database ["+DATABASE_NAME+"].");
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            println("Upgrading Database from version "+oldVersion+" to "+newVersion+".");
            //만약 옛날 버전이 2보다 작다면 (첫 번째 버전이었다면?)
            if(oldVersion < 2){

            }
        }

        private void insertRecord(SQLiteDatabase _db, String title, String author, String contents){
            try{
                _db.execSQL("INSERT INTO "+TABLE_BOOK_INFO+" (title, author, contents) VALUES ('" +title + "', '" + author + "', '" + contents + "')");
            }catch (Exception e){
                Log.e(TAG, "Exception in dbHelper.insertRecord()",e);
            }
        }
    }

}
