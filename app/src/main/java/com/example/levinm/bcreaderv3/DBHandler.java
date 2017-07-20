package com.example.levinm.bcreaderv3;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by levinm on 10/07/2017.
 */

public class DBHandler extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String TABLE = "Product";


    //Table Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_Product = "ProductName";
    public static final String Key_ProductBarcode = "barcode";


    //Table Name


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("creating: ","Inserting.. " );


        String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE + " (" +
                KEY_ID + " TEXT PRIMARY KEY," +
                KEY_Product + " TEXT," +
                Key_ProductBarcode + " TEXT)";


        db.execSQL(CREATE_TABLE_PRODUCTS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int NewVersion) {

        //Drop Older table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        //Recreate
        onCreate(db);
    }

    public void insert(Context context){


        SQLiteDatabase db = getWritableDatabase();

        String mCSVfile = "csvfile.csv";
        AssetManager manager = context.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.beginTransaction();

        try {
            while ((line = buffer.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length < 1) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }

                ContentValues values = new ContentValues();
                values.put(KEY_ID, columns[0].trim());
                values.put(Key_ProductBarcode, columns[1].trim());
                values.put(KEY_Product, columns[2].trim());
                db.insert(this.TABLE, null, values);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }

    public List<Product> getAllProducts(){


        List<Product> productlist = new ArrayList<Product>();
        String Select = "SELECT * FROM " + TABLE;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(Select, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId((cursor.getString(0)));
                product.setName(cursor.getString(1));
                product.setBarcode(cursor.getString(2));

                productlist.add(product);

            } while (cursor.moveToNext());

        }
        return productlist;
    }


    public Product getProduct(String barcode) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{KEY_ID, Key_ProductBarcode,
                KEY_Product}, Key_ProductBarcode + "=?", new String[]{barcode}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        Product contact = new Product(cursor.getString(0), cursor.getString(2), cursor.getString(1));
        if (contact == null){

            return null;
        }

        else {
            return contact;
        }

    }


}