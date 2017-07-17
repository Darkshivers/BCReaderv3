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

    SharedPreferences historyshared;
    ArrayList<String> historyitems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        int MY_PERMISSION_REQUEST_CAMERA = 0;
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                initiateintegrator();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
            }
        }


        //On Create
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        textchangeupdate();
        assignbuttons("");
        historyshared = getSharedPreferences("Historyshared", MODE_PRIVATE);
        retreivevalues();

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        historyitems = savedInstanceState.getStringArrayList("History");
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void saveData() {

        SharedPreferences.Editor edit = historyshared.edit();
        Set<String> set = new HashSet<>();
        set.addAll(historyitems);
        edit.putStringSet("History Items", set);
        edit.apply();
        Log.d("Stored Preferences set", "" +set);
    }

    private void retreivevalues(){

        Set <String> set = historyshared.getStringSet("History Items", null);
        historyitems.addAll(set);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                TextView DigitalBarcoderesults = (TextView) findViewById(R.id.BarcodeResult);
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                DigitalBarcoderesults.setText("Barcode Result: " + result.getContents());

                historyitems.add(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        String barcode = result.getContents();
        assignbuttons(barcode);

    }


    //Check text box for text updates reference DB
    public void textchangeupdate() {

        final EditText PhysicalBarcode = (EditText) findViewById(R.id.editphystxt);
        final TextView physicalText = (TextView) findViewById(R.id.physicalscan);

        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String Str = PhysicalBarcode.getText().toString();
                physicalText.setText(Str);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        PhysicalBarcode.addTextChangedListener(inputTextWatcher);
    }

    //Assign buttons to switches
    public void assignbuttons(String barcode) {

        final String searchedbarcode = barcode;
        Button clear = (Button) findViewById(R.id.barcode);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int MY_PERMISSION_REQUEST_CAMERA = 0;
                initiateintegrator();
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                        initiateintegrator();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
                    }
                }

            }
        });

        final TextView BarcodeText = (TextView) findViewById(R.id.BarcodeResult);
        Button button = (Button) findViewById(R.id.clear);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BarcodeText.setText("");
                assignbuttons("");
            }
        });


        Button search = (Button) findViewById(R.id.buttonsearch);
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


        Button history = (Button) findViewById(R.id.btnHistory);
        history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {

                    saveData();
                    Intent intent = new Intent(MainActivity.this, historyactivity.class);
                    intent.putStringArrayListExtra("key" , historyitems);
                    startActivity(intent);
            }
        });


        Button scannedbc = (Button) findViewById(R.id.btnProdScan);
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
    //        switch (event.getKeyCode()) {
//
//            case KeyEvent.KEYCODE_0:
//                MyDigits.add(0);
//                break;
//
//            case KeyEvent.KEYCODE_1:
//                MyDigits.add(1);
//                break;
//
//            case KeyEvent.KEYCODE_2:
//                MyDigits.add(2);
//                break;
//
//            case KeyEvent.KEYCODE_3:
//                MyDigits.add(3);
//                break;
//
//            case KeyEvent.KEYCODE_4:
//                MyDigits.add(4);
//                break;
//
//            case KeyEvent.KEYCODE_5:
//                MyDigits.add(5);
//                break;
//
//            case KeyEvent.KEYCODE_6:
//                MyDigits.add(6);
//                break;
//
//            case KeyEvent.KEYCODE_7:
//                MyDigits.add(7);
//                break;
//
//            case KeyEvent.KEYCODE_8:
//                MyDigits.add(8);
//                break;
//
//            case KeyEvent.KEYCODE_9:
//                MyDigits.add(9);
//                break;
//
//
//        }



