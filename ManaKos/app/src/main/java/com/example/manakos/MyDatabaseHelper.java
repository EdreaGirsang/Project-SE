package com.example.manakos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper{

    private Context context;
    public static  final String DATABASE_NAME = "Manakos.db";
    public static final  int DATABASE_VERSION = 1;

    public static  final String TABLE_NAME = "Kos";
    public static final  String Column_RoomUse = "Room";
    public static  final String Column_KosId = "ID";
    public static  final String Column_KosName = "Name";

    public static final String TableUser = "Account";
    public static final String Column_Email = "Email";
    public static final String Column_Pass = "Password";

    public static final String Column_Name = "Username";
    public static  final String Id = "ID";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String account = "CREATE TABLE " + TableUser + " (" + Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Column_Email + " TEXT UNIQUE, " + Column_Name + " TEXT UNIQUE, " + Column_Pass + " TEXT);";
        String query = "CREATE TABLE " + TABLE_NAME + " (" + Column_KosId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Id + " INTEGER, "
                + Column_KosName + " TEXT, " + Column_RoomUse + " INTEGER);";
        db.execSQL(account);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void add_kos(String name, int room, int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Column_KosName, name);
        cv.put(Column_RoomUse, room);
        cv.put(Id, userId);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }
}
