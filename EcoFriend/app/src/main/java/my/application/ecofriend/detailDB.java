package my.application.ecofriend;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import my.application.ecofriend.datas.PdfItem;

public class detailDB extends SQLiteOpenHelper {
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public detailDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DetailList (date TEXT, selectdate TEXT, classification TEXT, item TEXT, size TEXT, price LONG, count LONG, total LONG, state LONG);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table DetailList;"; // 테이블 드랍
        db.execSQL(sql);
        onCreate(db); // 다시 테이블 생성
    }

    public ArrayList<PdfItem> query(String key) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<PdfItem> mDetailList = new ArrayList<PdfItem>();

        Cursor cursor = db.rawQuery("SELECT * FROM DetailList WHERE date='" + key + "'", null);
        while (cursor.moveToNext()) {
            mDetailList.add(
                    new PdfItem(cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getLong(5),
                            cursor.getLong(6))
            );
        }
        db.close();

        return mDetailList;
    }

    public void insert(String date, String selectdate, String classification, String item, String size, Long price, Long count, Long total, Long state) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO DetailList VALUES('" + date + "', '" + selectdate + "', '" + classification + "', '" + item + "', '" + size + "', '" + price + "', '" + count + "', '" + total + "', '" + state + "');");
        db.close();
    }

    public void update(String key, int state) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DetailList SET state='" + state + "' WHERE date='" + key + "';");
        db.close();
    }

    public void delete(String key) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM DetailList WHERE date='" + key + "';");
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
//        // 입력한 항목과 일치하는 행 삭제
//        db.execSQL("DELETE FROM ItemList;");
//        db.close();
        String sql = "drop table DetailList;"; // 테이블 드랍
        db.execSQL(sql);
        onCreate(db); // 다시 테이블 생성

    }

    public String getSelectDate(String key) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        String result = null;
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT selectdate FROM DetailList WHERE date='" + key + "'", null);
        cursor.moveToNext();
        if(cursor.getString(0) != null){
            result = cursor.getString(0);
        }
        db.close();

        return result;
    }

    public long size() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        long result = -1;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM DetailList", null);
        while (cursor.moveToNext()) {
            result+=1;
        }

        return result;
    }

    public Long getState(String key) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT state FROM DetailList WHERE date='" + key + "'", null);

        long result = 1;
        while (cursor.moveToNext()) {
            if(cursor.getLong(0) != 1){
                result = cursor.getLong(0);
            }
        }
        db.close();
        return result;
    }
}

