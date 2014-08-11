package jpttrindade.br.gdrivetest.models.repositorios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by joaotrindade on 07/08/14.
 */
public class SQLiteBDHelper extends SQLiteOpenHelper {



    public static final String DATE_FORMAT = "EEEE, dd/MM/yyyy/hh:mm:ss";



    private static final String DATABASE_NAME = "gdrivebd";
    private static final int DATABASE_VERSION = 1 ;


    private ArrayList<String> CREATE;

    private static SQLiteBDHelper singleton;
    Context mContext;


    private SQLiteBDHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context.getApplicationContext();
     //   CREATE = createScript;
    }

    public static SQLiteBDHelper getInstance(Context context){
        if(singleton == null){
            singleton = new SQLiteBDHelper(context);
        }

        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //new CreateBDAsyncTask(mContext, db).execute();

        for(String script : SCRIPTS.CREATE){
            db.execSQL(script);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
