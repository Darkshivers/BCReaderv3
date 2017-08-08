package com.example.levinm.bcreaderv3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String defaultusername = "username";
    String defaultpassword = "password";
    EditText Username;
    EditText Password;
    TextView response;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText)findViewById(R.id.edtName);
        Password = (EditText)findViewById(R.id.edtPass);
        response = (TextView)findViewById(R.id.tvResult);
        submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(this);

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
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you wish to assign this account code to the device?").setPositiveButton("Yes", dialogClickListener)
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
