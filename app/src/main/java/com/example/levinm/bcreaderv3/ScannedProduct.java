package com.example.levinm.bcreaderv3;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class ScannedProduct extends AppCompatActivity {

    ListView lvProduct;
    Context context;
    ArrayList wheelname;



    public static int[] wheelimages= {R.drawable.davanti, R.drawable.evergreen};
    public static String[] wheelnamelist={"Davanti", "Evergreen"};

    DBHandler dbhandler = new DBHandler(ScannedProduct.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_product);

        String barcode = "5060408160299";
        Product product =  dbhandler.getProduct(barcode);
        String prodName = product.getName();
        String prodId = product.getId();
        String prodBrand = product.getBrand();

        productbrand(product.getBrand());

        Log.d("Product::", "ProdName: " + prodName + " Brand: " + prodBrand + " Product ID: " + prodId);



        lvProduct=(ListView) findViewById(R.id.lvProduct);
        lvProduct.setAdapter(new CustomAdapter(this, wheelnamelist, wheelimages));
        lvProduct.setDivider(null);
        lvProduct.setDividerHeight(0);
    }

    public void productbrand(String product) {

            if (product.matches("DT")) {
                Log.d("Update::", "Product Brand is Assigned to: " + product);
            }

            else if (product.matches("DA")){
            Log.d("Update::", "Product Brand is Assigned to: " + product);
        }

            else if (product == null) {
                Log.d("Update::", "No Product Passed");
            }

            else {
                Log.d("Update::", "No Conditions Apply " + " Product = " + product);
            }

    }

}
