package com.example.levinm.bcreaderv3;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by levinm on 26/07/2017.
 */

//This class checks the existence of the DB
public class DBChecker {

    public DBChecker(){}
    Context context;
    ProductPoll query = new ProductPoll();
    DBHandler db = new DBHandler(context);

    public void doesDBExist(Context context, String csv) {

        Log.d("Check:", "Checking DB...");
        String db_name = db.DATABASE_NAME;
        File dbFile = context.getDatabasePath(db_name); //Check to see if the Database exists

        if (dbFile.exists() == false) { //The Database doesn't exist!
            Log.d("Check:", "Database not active");
            query.insert(context, csv);
        }

        if (dbFile.exists() == true) { //The Database exists!
            Log.d("Check:", "Database Active");
            query.getProducts(context);
        }
        else {
            Log.d("Check:", "Not checked"); //Database not checked for condition
        }
    }
}



