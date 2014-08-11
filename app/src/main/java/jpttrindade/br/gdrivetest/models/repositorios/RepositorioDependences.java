package jpttrindade.br.gdrivetest.models.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import jpttrindade.br.gdrivetest.models.Requisito;

/**
 * Created by jpttrindade on 10/08/14.
 */
public class RepositorioDependences {

    private static RepositorioDependences singleton;
    private Context mContext;
    private SQLiteDatabase mDB;

    private RepositorioDependences(Context context){
        mContext = context.getApplicationContext();
        mDB = SQLiteBDHelper.getInstance(context).getWritableDatabase();
    }

    public static RepositorioDependences getInstance(Context context){
        if(singleton == null){
            singleton = new RepositorioDependences(context);
        }
        return  singleton;
    }

    public long addDependence(Requisito req, Requisito dependent){
        ContentValues values = new ContentValues();

        values.put(SCRIPTS.DEPENDENCE_ID_REQUIREMENT, req.getId());
        values.put(SCRIPTS.DEPENDENCE_ID_PROJECT, req.getProjeto().getId());
        values.put(SCRIPTS.DEPENDENCE_ID_DEPENDENT, dependent.getId());

        return mDB.insert(SCRIPTS.TABLE_DEPENDENCE,null, values);
    }

    public ArrayList<String> getDependents(String id_requirement, String id_project) {

        ArrayList<String> ids_dependents= new ArrayList<String>();
        ArrayList<Requisito> dependents = new ArrayList<Requisito>();

        String query = new QUERY().select(SCRIPTS.DEPENDENCE_ID_DEPENDENT).from(SCRIPTS.TABLE_DEPENDENCE)
                                    .where().whereClause(SCRIPTS.DEPENDENCE_ID_REQUIREMENT,id_requirement)
                                    .and().whereClause(SCRIPTS.DEPENDENCE_ID_PROJECT, id_project).finish();

        Cursor c = mDB.rawQuery(query, null);
        /*Cursor c = mDB.rawQuery("select "+SCRIPTS.DEPENDENCE_ID_DEPENDENT+" from "+SCRIPTS.TABLE_DEPENDENCE+
                            " where "+SCRIPTS.DEPENDENCE_ID_REQUIREMENT+"="+id_requirement+
                            " and "+SCRIPTS.DEPENDENCE_ID_PROJECT+"="+id_project+";", null);
*/
        if(c.getCount()>0){
            c.moveToFirst();
            int x;
            do{
                x = c.getInt(c.getColumnIndex(SCRIPTS.DEPENDENCE_ID_DEPENDENT));

                Log.d("DEBUG", "id dependent - " +x);

                ids_dependents.add(""+x);
            }while (c.moveToNext());
        }
        c.close();



        return ids_dependents;


    }



    private class Dependents{
        String id_requirement;
        String id_projeto;
        String id_dependent;
    }
}
