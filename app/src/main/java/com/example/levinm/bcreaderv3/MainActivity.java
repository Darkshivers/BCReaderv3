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
import android.app.Activity;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences historyshared ;
    ArrayList < String > historyitems = new ArrayList <String> ();
    TextView username;
    String Username;
    int amountstored = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // On Create
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        assignbuttons(""); //Calls Buttons method
        textchangeupdate(); //Call Text change method

        username = (TextView) findViewById(R.id.tvUsername);


        historyshared = getSharedPreferences("Historyshared", MainActivity.MODE_PRIVATE); //Sets shared preferences

        getStrings("AndroidID","");
        getStrings("Username","");

        //retreivevalues(); //Retrieve Preference Values

    }

    public boolean requestpermission() { //Request Permission to use camera when contextual

        int MY_PERMISSION_REQUEST_CAMERA = 0;
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                initiateintegrator();
                return false;
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]

                        {

                        Manifest.permission.CAMERA
                }, MY_PERMISSION_REQUEST_CAMERA);
            }
        }
        return true;
    }


    @Override
    //Keeps hold of data when application is updated
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        historyitems = savedInstanceState.getStringArrayList("History");
        super.onRestoreInstanceState(savedInstanceState);
    }

    //Saves scanned bar codes
    private void saveData(String value) {

        SharedPreferences sp = getSharedPreferences("history", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        amountstored = sp.getAll().size();
        String amountstoredString = Integer.toString(amountstored++);
        editor.putString(amountstoredString, value);
        Log.d("Preferences Saved", value);
        editor.commit();

    }

    //Recieves scanned barcodes from saved preferences
    public void getStrings(String key, String value){
        SharedPreferences sp = getSharedPreferences("user_preferences", Activity.MODE_PRIVATE);
        String savedValue = sp.getString(key, value);
        Log.d("MainResult", "Result = " + savedValue);
        username.setText("Hello! " + savedValue);
        Username = savedValue;
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
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                TextView DigitalBarcoderesults = (TextView) findViewById(R.id.BarcodeResult);

                //Being DB Conn
                DBHandler db = new DBHandler(MainActivity.this);

                String barcode = result.getContents();
                Product product = db.getProduct(barcode);
                DigitalBarcoderesults.setText(product.getName());

                historyitems.add(barcode);
                saveData(barcode);
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
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            public void afterTextChanged(Editable s) {
                String Str = PhysicalBarcode.getText().toString();
                if (Str.length() >= 13) {
                    Product product = db.getProduct(Str);
                    physicalText.setText(product.getName());
                    PhysicalBarcode.setText("");

                    Intent intent = new Intent(MainActivity.this, ProductScan.class);
                    intent.putExtra("Barcode", Str);
                    startActivity(intent);
                }
            }
        };

        //Assigns text listener to EditText
        PhysicalBarcode.addTextChangedListener(inputTextWatcher);
    }

    //Assign buttons to switches
    public void assignbuttons(final String barcode) {

        //Button inits
        final TextView BarcodeText = (TextView) findViewById(R.id.BarcodeResult); //    Textview for barcode
        Button search = (Button) findViewById(R.id.buttonsearch); // Search the internet
        Button history = (Button) findViewById(R.id.btnHistory); //Check barcode scan history
        Button scannedbc = (Button) findViewById(R.id.btnProdScan); //The same ^
        Button button = (Button) findViewById(R.id.clear); //Clear barcode scans, prevents searching
        Button debugTest = (Button) findViewById(R.id.btnDebug); //Debug button

        final String bc = barcode;
        final String searchedbarcode = barcode;

        debugTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView DigitalBarcoderesults = (TextView) findViewById(R.id.BarcodeResult);
                DBHandler db = new DBHandler(MainActivity.this);

                if (barcode == "") { //Avon
                    Product product = db.getProduct("29142485506");
                    DigitalBarcoderesults.setText(product.getName());
                    assignbuttons("29142485506");
                    return;

                }

                if (barcode == "29142485506") { //  Davanti
                    Product product = db.getProduct("5060408160299");
                    DigitalBarcoderesults.setText(product.getName());
                    assignbuttons("5060408160299");
                    return;

                }

                if (barcode == "5060408160299") { // Evergreen
                    Product product = db.getProduct("69222504404882");
                    DigitalBarcoderesults.setText(product.getName());
                    assignbuttons(null);
                    return;
                }
            }
        });


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
                startActivity(intent);
            }
        });

        scannedbc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent intent = new Intent(MainActivity.this, ProductScan.class);
                intent.putExtra("Barcode", bc);
                startActivity(intent);
            }
        });

    }

    public void initiateintegrator() {

        if (requestpermission() == false) {
            requestpermission();
        }

        else {
            //Configure ZXing embedded
            Log.d("i", "Button Pressed");
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setBarcodeImageEnabled(true);
            integrator.setPrompt("Scan the barcode on the tyre");
            //Initiate Scanner
            integrator.initiateScan();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //USB plugged
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "Barcode Scanner detected. Virtual Keyboard Disabled", Toast.LENGTH_LONG).show();


        }
        //USB Unplugged
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "Barcode Scanner Removed. Virtual Keyboard Enabled", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("History", historyitems);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {

    }
}




