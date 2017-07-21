/*Copyright (C) 2012-2017 ZXing authors, Journey Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package com.example.levinm.bcreaderv3;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    SharedPreferences historyshared ;
    ArrayList < String > historyitems = new ArrayList <String> ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ProductPoll query = new ProductPoll();

        //Requests permissions to use camera for new versions of android
        int MY_PERMISSION_REQUEST_CAMERA = 0;
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                initiateintegrator();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                        Manifest.permission.CAMERA
                }, MY_PERMISSION_REQUEST_CAMERA);
            }
        }

        // On Create
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        query.getProducts(MainActivity.this);


        //Calls Buttons method
        assignbuttons("");

        //Call Text change method
        textchangeupdate();

        //Sets shared preferences
        historyshared = getSharedPreferences("Historyshared", MainActivity.MODE_PRIVATE);


    }

    @Override
    //Keeps hold of data when application is updated
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        historyitems = savedInstanceState.getStringArrayList("History");
        super.onRestoreInstanceState(savedInstanceState);
    }

    //Saves scanned bar codes
    private void saveData() {

        SharedPreferences.Editor edit = historyshared.edit();
        Set < String > set = new HashSet < > ();
        set.addAll(historyitems);
        edit.putStringSet("History Items", set);
        edit.apply();
        Log.d("Stored Preferences set", "" + set);
    }

    //Recieves scanned barcodes from saved preferences
    private void retreivevalues() {

        Set < String > set = historyshared.getStringSet("History Items", null);
        historyitems.addAll(set);

    }

    @Override
    //Once ZXing has returned back to MainActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //Checks if the user has scanned any product
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Scanned product and stores it within textview
                TextView DigitalBarcoderesults = (TextView) findViewById(R.id.BarcodeResult);
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                //Being DB Conn
                DBHandler db = new DBHandler(MainActivity.this);

                String barcode = result.getContents();
                Product product = db.getProduct(barcode);
                DigitalBarcoderesults.setText(product.getName());

                historyitems.add(barcode);
                saveData();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        //Sets barcode result and stores it into a string
        String barcode = result.getContents();

        //sends barcode to button for when pressed
        assignbuttons(barcode);
    }


    //Check text box for text updates reference DB
    public void textchangeupdate() {

        //Gained from physical barcode
        final EditText PhysicalBarcode = (EditText) findViewById(R.id.editphystxt);

        //Stores product name
        final TextView physicalText = (TextView) findViewById(R.id.physicalscan);

        //Init DBase
        final DBHandler db = new DBHandler(this);

        //update text with barcode name
        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String Str = PhysicalBarcode.getText().toString();
                if (Str.length() >= 13) {
                    Product product = db.getProduct(Str);
                    physicalText.setText(product.getName());
                    PhysicalBarcode.setText("");
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        //Assigns text listener to button
        PhysicalBarcode.addTextChangedListener(inputTextWatcher);
    }

    //Assign buttons to switches
    public void assignbuttons(String barcode) {

        //Button inits
        final TextView BarcodeText = (TextView) findViewById(R.id.BarcodeResult); //    Textview for barcode
        Button search = (Button) findViewById(R.id.buttonsearch); // Search the internet
        Button history = (Button) findViewById(R.id.btnHistory); //Check barcode scan history
        Button scannedbc = (Button) findViewById(R.id.btnProdScan); //The same ^
        Button button = (Button) findViewById(R.id.clear); //Clear barcode scans, prevents searching

        final String searchedbarcode = barcode;
        Button clear = (Button) findViewById(R.id.barcode);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initiateintegrator();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BarcodeText.setText("");
                assignbuttons("");
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {

                if (searchedbarcode != ("")) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    String keyword = searchedbarcode;
                    intent.putExtra(SearchManager.QUERY, keyword);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No Barcode Scanned", Toast.LENGTH_LONG).show();
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent intent = new Intent(MainActivity.this, historyactivity.class);
                intent.putStringArrayListExtra("key", historyitems);
                startActivity(intent);
            }
        });

        scannedbc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent intent = new Intent(MainActivity.this, ScannedProduct.class);
                startActivity(intent);
            }
        });
    }

    public void initiateintegrator() {

        //Configure ZXing embedded
        Log.d("i", "Button Pressed");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setBarcodeImageEnabled(true);
        integrator.setPrompt("Scan the barcode on the tyre");

        //Initiate Scanner
        integrator.initiateScan();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //USB plugged
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "Barcode Scanner detected.", Toast.LENGTH_LONG).show();


        }
        //USB Unplugged
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "Barcode Scanner Removed.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("History", historyitems);
        super.onSaveInstanceState(outState);
    }
}




