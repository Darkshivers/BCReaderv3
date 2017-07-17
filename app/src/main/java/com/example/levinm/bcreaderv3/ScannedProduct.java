package com.example.levinm.bcreaderv3;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class ScannedProduct extends AppCompatActivity {

    ListView lvProduct;
    Context context;
    ArrayList wheelname;
    public static int[] wheelimages= {R.drawable.davanti, R.drawable.evergreen, R.drawable.landsail, R.drawable.davanti, R.drawable.landsail, R.drawable.evergreen};
    public static String[] wheelnamelist={"Davanti" , "EverGreen", "LandSail", "Davanti", "LandSail","EverGreen"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_product);

        lvProduct=(ListView) findViewById(R.id.lvProduct);
        lvProduct.setAdapter(new CustomAdapter(this, wheelnamelist, wheelimages));
        lvProduct.setDivider(null);
        lvProduct.setDividerHeight(0);
    }

}
