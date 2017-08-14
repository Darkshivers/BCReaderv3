package com.example.levinm.bcreaderv3;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductScan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_scan);
        DBHandler dbhandler = new DBHandler(this);


        SharedPreferences sp = getSharedPreferences("history", Activity.MODE_PRIVATE);
        int amount = sp.getAll().size() - 1;
        String StrAmount = Integer.toString(amount);
        Log.d("ScannedAmount:", StrAmount);

        String bc = sp.getString(StrAmount,"");
        Log.d("ProductScanCheck: ", bc);

        Product product =  dbhandler.getProduct(bc, this);
        TextView name = (TextView) findViewById(R.id.tvprodname);

        name.setText(product.getName());
        checkbrand(product.getBrand());

    }

    public String checkbrand(String brand) {

        ImageView imgbrand = (ImageView) findViewById(R.id.imgbrand);
        switch(brand) {
            case "DA": imgbrand.setImageResource(R.drawable.davanti);
                break;
            case "EV": imgbrand.setImageResource(R.drawable.evergreen);
                break;
            case "LS": imgbrand.setImageResource(R.drawable.landsail);
                break;
            default: imgbrand.setImageResource(R.drawable.logo);
        }
        return brand;
    }


}
