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

    //Database information
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    //Table Name
    private static final String TABLE = "Product";
    private static final String PRICE_TABLE = "Price";


    //Product Table Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_Product = "ProductName";
    public static final String Key_ProductBarcode = "barcode";
    public static final String Key_Brand = "brand";

    //Price table

    public static final String Key_Price_ID = "id";
    public static final String KEY_price = "price";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("creating: ","Inserting.. " );

        //Database creation query
        String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE + " (" +
                KEY_ID + " TEXT PRIMARY KEY," +
                KEY_Product + " TEXT," +
                Key_ProductBarcode + " TEXT," +
                Key_Brand + " TEXT)";

        String CREATE_TABLE_PRICE = "CREATE TABLE " + PRICE_TABLE + " (" +
                Key_Price_ID + "TEXT PRIMARY KEY," +
                KEY_price + " TEXT";

        db.execSQL(CREATE_TABLE_PRODUCTS); //Exec products query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int NewVersion) {

        //Drop Older table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        //Recreate
        onCreate(db);
    }

    //Inserts products into DB when called
    public void insert(Context context, String csv){

        SQLiteDatabase db = getWritableDatabase(); //Grab editiable database

        String mCSVfile = csv; //Sets name of CSV file
        AssetManager manager = context.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line;
        db.beginTransaction();

        try {
            //Prevents errors
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
                values.put(Key_Brand, columns[3].trim());
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
                product.setBrand(cursor.getString(3));
                productlist.add(product);

            } while (cursor.moveToNext());

        }
        return productlist;
    }

    public Product getProduct(String barcode) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{KEY_ID, Key_ProductBarcode,
                KEY_Product, Key_Brand}, Key_ProductBarcode + "=?", new String[]{barcode}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
            Product contact = new Product(cursor.getString(0), cursor.getString(2), cursor.getString(1), cursor.getString(3));
        if (contact == null){
            cursor.close();
            return null;
        }
        else {
            return contact;
        }
    }
}