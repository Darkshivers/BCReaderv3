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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView username, physicalbc, camerabc;
    String Username, storedbarcode;
    int amountstored = 0;
    Button search, history, scannedbc, button, barcode;
    DBHandler db = new DBHandler(this);
    ProductPoll query = new ProductPoll();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // On Create
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        textchangeupdate(); //Call Text change method

        username = (TextView) findViewById(R.id.tvUsername);

        search = (Button) findViewById(R.id.buttonsearch); // Search the internet
        search.setOnClickListener(this);

        history = (Button) findViewById(R.id.btnHistory); //Check barcode scan history
        history.setOnClickListener(this);

        scannedbc = (Button) findViewById(R.id.btnProdScan); //The same ^
        scannedbc.setOnClickListener(this);

        button = (Button) findViewById(R.id.clear); //Clear barcode scans, prevents searching
        button.setOnClickListener(this);

        barcode = (Button) findViewById(R.id.barcode); //
        barcode.setOnClickListener(this);

        physicalbc = (TextView) findViewById(R.id.physicalscan);
        camerabc = (TextView) findViewById(R.id.BarcodeResult);

        getStrings("Username", null);
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

    //Saves scanned bar codes
    private void saveData(String value) {
        SharedPreferences sp = getSharedPreferences("history", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        amountstored = sp.getAll().size();
        String amountstoredString = Integer.toString(amountstored++);
        editor.putString(amountstoredString, value);
        storedbarcode = value;
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

//                String barcode = result.getContents();
//                Product product = db.getProduct(barcode, this);
//                DigitalBarcoderesults.setText(product.getName());

                storedbarcode = result.getContents();
                DigitalBarcoderesults.setText(query.getProductName(storedbarcode, this));

                saveData(storedbarcode);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Check text box for text updates reference DB
    public void textchangeupdate() {
        //Gained from physical barcode
        final EditText PhysicalBarcode = (EditText) findViewById(R.id.editphystxt);
        //update text with barcode name
        TextWatcher inputTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            public void afterTextChanged(Editable s) {
                String Str = PhysicalBarcode.getText().toString();
                if (Str.length() >= 13) {
                    Product product = db.getProduct(Str, MainActivity.this);
                    physicalbc.setText(product.getName());
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
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {

            case R.id.buttonsearch:
                if (storedbarcode != ("")) {
                    intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, storedbarcode);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No Barcode Scanned", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnHistory:
                intent = new Intent(MainActivity.this, historyactivity.class);
                startActivity(intent);
                break;

            case R.id.btnProdScan:
                if (storedbarcode == null){
                    Toast.makeText(this, "No Product Scanned", Toast.LENGTH_LONG).show();
                    break;
                }
                else {
                    intent = new Intent(MainActivity.this, ProductScan.class);
                    startActivity(intent);
                    break;
                }

            case R.id.clear:
                physicalbc.setText("");
                camerabc.setText("");
                break;

            case R.id.barcode:
                initiateintegrator();
                break;
        }
    }
}