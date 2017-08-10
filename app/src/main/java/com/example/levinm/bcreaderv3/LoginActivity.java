package com.example.levinm.bcreaderv3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String defaultusername = "Matthew", defaultpassword = "password", android_id;
    EditText Username, Password;
    TextView response;
    Button submit;
    DBChecker check = new DBChecker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText)findViewById(R.id.edtName);
        Password = (EditText)findViewById(R.id.edtPass);
        response = (TextView)findViewById(R.id.tvResult);
        submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(this);
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("DeviceID", android_id);
        getStrings("Username",""); //Check to see if the device has been assigned to an account
    }

    public void SaveString(String key, String value){

        SharedPreferences sp = getSharedPreferences("user_preferences", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        Log.d("Saved Result", "Key: " + key + " Value: " + value);
        editor.commit();

    }

    public void getStrings(String key, String value){

        SharedPreferences sp = getSharedPreferences("user_preferences", Activity.MODE_PRIVATE);
        String savedValue = sp.getString(key, value);

        if (savedValue.length() <= 1){
            Log.d("Result", "No User Assigned to device");
        }

        else {
            Log.d("Result", "Result = " + savedValue);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public boolean checkcontents() {

        if (Username.getText().toString().equals("") || Username.getText().toString().equals("")) {
            Log.d("Login: ", "Please fill in the customers username");
            response.setText("Please fill in customers details");
            return false;
        }

        else {
            Log.d("Login: ", "Fields have content");
            return true;
        }

    }

    public void checkDetails() {

        if (Username.getText().toString().equals(defaultusername) && Password.getText().toString().equals(defaultpassword)) {
            response.setText("Account Details Correct");
            Log.d("Login: ", "Access Granted");

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            check.doesDBExist(LoginActivity.this, "csvfile.csv"); //Check Database Exists and create Products table

                            SaveString("AndroidID", android_id);
                            SaveString("Username", Username.getText().toString());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            Username.setText(""); Password.setText("");
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you wish to assign this account code to the device?").setPositiveButton("Assign", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }

        else {
            response.setText("Account Details Incorrect");
            Log.d("Login: ", "Incorrect Details Entered");
        }

    }

    @Override
    public void onClick(View view) {

            if (checkcontents() == true){
                checkDetails();
            }
            else {
                Log.d("Login: ", "Please Insert details");
            }
    }
}
