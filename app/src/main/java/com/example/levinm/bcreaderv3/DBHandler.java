package com.example.levinm.bcreaderv3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by levinm on 10/07/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "crud.db";


    public DBHandler(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + Products.TABLE + "("
                + Products.KEY_ID + "INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Products.KEY_Product + "TEXT, "
                + Products.Key_ProductBarcode + "INTEGER)";

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop Older table
        db.execSQL("DROP TABLE IF EXISTS " + Products.TABLE);

        //Recreate
        onCreate(db);
    }
}