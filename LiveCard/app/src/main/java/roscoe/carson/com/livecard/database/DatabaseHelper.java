package roscoe.carson.com.livecard.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carson on 11/6/2016.
 */
public final class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "LiveCard.db";
    public static final int DATABASE_VERSION = 5;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CardTable.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CardTable.DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void cleanTable(SQLiteDatabase db) {
        db.execSQL(CardTable.DELETE_TABLE);
        onCreate(db);
    }
}
