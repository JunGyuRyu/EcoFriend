package my.application.ecogreen;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import my.application.ecogreen.datas.CheckedItem;
import my.application.ecogreen.datas.PdfItem;

public class DBHelper extends SQLiteOpenHelper {
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 ItemList이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, size create_at 컬럼, price 정수형 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE ItemList (classification TEXT, dockey TEXT, item TEXT, size TEXT, price LONG, count LONG);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table ItemList;"; // 테이블 드랍
        db.execSQL(sql);
        onCreate(db); // 다시 테이블 생성
    }

    public long query(String key) {
        SQLiteDatabase db = getReadableDatabase();
        long result = -1;

        Cursor cursor = db.rawQuery("SELECT count FROM ItemList WHERE dockey='" + key + "'", null);
        cursor.moveToNext();
        //Log.d("cursor", String.valueOf(cursor.getCount()));
        if(cursor.getCount() != 0){
            result = cursor.getLong(0);
        }
        db.close();

        return result;
    }

    public void insert(String classification, String dockey, String item, String size, Long price, Long count) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO ItemList VALUES('" + classification + "', '" + dockey + "', '" + item + "', '" + size + "', '" + price + "', '" + count + "');");
        db.close();
    }

    public void update(String key, int count) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE ItemList SET count='" + count + "' WHERE dockey='" + key + "';");
        db.close();
    }

    public void delete(String key) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM ItemList WHERE dockey='" + key + "';");
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
//        // 입력한 항목과 일치하는 행 삭제
//        db.execSQL("DELETE FROM ItemList;");
//        db.close();
        String sql = "drop table ItemList;"; // 테이블 드랍
        db.execSQL(sql);
        onCreate(db); // 다시 테이블 생성

    }

    public ArrayList<CheckedItem> getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<CheckedItem> mCheckedItemList = new ArrayList<CheckedItem>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM ItemList", null);
        while (cursor.moveToNext()) {
            mCheckedItemList.add(
                    new CheckedItem(cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getLong(4),
                            cursor.getLong(5))
            );
        }

        return mCheckedItemList;
    }

    public ArrayList<PdfItem> getDetail() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<PdfItem> mItemList = new ArrayList<PdfItem>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM ItemList", null);
        while (cursor.moveToNext()) {
            mItemList.add(
                    new PdfItem(cursor.getString(0),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getLong(4),
                            cursor.getLong(5))
            );
        }

        return mItemList;
    }


    public long size() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        long result = -1;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM ItemList", null);
        while (cursor.moveToNext()) {
           result+=1;
        }

        return result;
    }

}

