package com.example.levinm.bcreaderv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductScan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_scan);

        Bundle extras = getIntent().getExtras();
        String bc = extras.getString("Barcode");
        DBHandler dbhandler = new DBHandler(this);

        if (bc.length() <= 1) {
            Intent intent = new Intent(ProductScan.this, MainActivity.class);
            startActivity(intent);
            return;
        }

        else {
            Product product =  dbhandler.getProduct(bc);
            TextView name = (TextView) findViewById(R.id.tvprodname);

            name.setText(product.getName());
            checkbrand(product.getBrand());
        }



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
