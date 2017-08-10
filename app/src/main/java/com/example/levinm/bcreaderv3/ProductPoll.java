package com.example.levinm.bcreaderv3;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by levinm on 21/07/2017.
 */
//this class queries the DBHandler
public class ProductPoll {

    public ProductPoll(){}


    //When called inserts CSV file into the SQLite database
    public void insert(Context context, String csv)  {

        DBHandler db = new DBHandler(context);
        Log.d("insert: ", "Inserting products... ");
        db.insert(context, csv);
        db.close();

    }

    //When called outputs all products into log
    public void getProducts(Context context){

        DBHandler db = new DBHandler(context);
        List<Product> product = db.getAllProducts();

        for (Product product1 : product) {
            String log = "id: " + product1.getId() + ", Name: " + product1.getName() + " , Barcode: " + product1.getBarCode() + ", Brand: " + product1.getBrand();
            Log.d("Product::", log);
        }
    }

}




