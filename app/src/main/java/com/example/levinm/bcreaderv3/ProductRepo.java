package com.example.levinm.bcreaderv3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by levinm on 12/07/2017.
 */
public class ProductRepo {

    private DBHandler dbHandler;

    public ProductRepo(Context context) {
        dbHandler = new DBHandler(context);
    }

    public int insert(Products product) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Products.KEY_Product, product.name);
        values.put(Products.Key_ProductBarcode, product.barcode);

        long product_id = db.insert(Products.TABLE, null, values);
        return (int) product_id;
    }


    public void delete(int product_id) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete(Products.TABLE, Products.KEY_ID + "= ?", new String[]{String.valueOf(product_id)});
        db.close();

    }

    public void update(Products product) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Products.Key_ProductBarcode, product.barcode);
        values.put(Products.KEY_Product, product.name);

        db.update(Products.TABLE, values, Products.KEY_ID + "= ?", new String[]{String.valueOf(Products.KEY_ID)});
        db.close(); //Close DB
    }


    public ArrayList<HashMap<String, String>> getProductList() {

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery = "SELECT " + Products.KEY_ID + " , " + Products.KEY_Product + " , " + Products.Key_ProductBarcode + " FROM " + Products.TABLE;
        ArrayList<HashMap<String, String>> productlist = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> product = new HashMap<String, String>();
                product.put("id", cursor.getString(cursor.getColumnIndex(Products.KEY_ID)));
                product.put("name", cursor.getString(cursor.getColumnIndex(Products.KEY_Product)));
                productlist.add(product);

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return productlist;
    }

    public Products getProductById(int id){

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selectQuery = "SELECT " + Products.KEY_ID + "," + Products.KEY_Product + "," + Products.Key_ProductBarcode + " FROM " + Products.TABLE + " WHERE " + Products.KEY_ID + "=?";

        int iCount =0;
        Products product = new Products();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(id)});

        if (cursor.moveToFirst()){
            do {
                product.id = cursor.getInt(cursor.getColumnIndex(Products.KEY_ID));
                product.name = cursor.getString(cursor.getColumnIndex(Products.KEY_Product));
                product.barcode = cursor.getString(cursor.getColumnIndex(Products.KEY_Product));
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return product;


    }
}


