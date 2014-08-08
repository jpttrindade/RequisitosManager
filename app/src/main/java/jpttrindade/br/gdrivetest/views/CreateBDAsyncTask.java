package jpttrindade.br.gdrivetest.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jpttrindade.br.gdrivetest.models.SQLiteBDHelper;

/**
 * Created by jpttrindade on 07/08/14.
 */
public class CreateBDAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String SCRIPT = "script.sql";
    Context mContetx;
    SQLiteDatabase mDb;
    ProgressDialog dialog;

    public CreateBDAsyncTask(Context ctx, SQLiteDatabase db){
        mContetx = ctx;
        mDb = db;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //dialog = ProgressDialog.show(mContetx, "","wait...");
    }

    @Override
    protected Void doInBackground(Void... params) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    mContetx.getAssets().open(SCRIPT)));
            String s = "";
            while((s=reader.readLine()) != null){
               mDb.execSQL(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(dialog!=null){
         //   dialog.dismiss();
        }
    }
}
