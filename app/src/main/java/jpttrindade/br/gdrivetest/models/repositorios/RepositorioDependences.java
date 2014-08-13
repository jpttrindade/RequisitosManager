package jpttrindade.br.gdrivetest.models.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import jpttrindade.br.gdrivetest.models.Dependence;

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

    public long insertDependence(Dependence dependence){
        ContentValues values = new ContentValues();

        values.put(SCRIPTS.DEPENDENCE_ID_REQUIREMENT, dependence.getId_requirement());
        values.put(SCRIPTS.DEPENDENCE_ID_PROJECT, dependence.getId_project());
        values.put(SCRIPTS.DEPENDENCE_ID_DEPENDENT, dependence.getId_dependent());

        return mDB.insert(SCRIPTS.TABLE_DEPENDENCE,null, values);
    }

    public ArrayList<Dependence> getDependencies(String id_requirement, String id_project) {


        ArrayList<Dependence> dependencies = new ArrayList<Dependence>();

        String query = new QUERY().select(SCRIPTS.DEPENDENCE_ID_DEPENDENT).from(SCRIPTS.TABLE_DEPENDENCE)
                                    .where().whereClause(SCRIPTS.DEPENDENCE_ID_REQUIREMENT,id_requirement)
                                    .and().whereClause(SCRIPTS.DEPENDENCE_ID_PROJECT, id_project)
                                    .finish();

        Cursor c = mDB.rawQuery(query, null);
        /*Cursor c = mDB.rawQuery("select "+SCRIPTS.DEPENDENCE_ID_DEPENDENT+" from "+SCRIPTS.TABLE_DEPENDENCE+
                            " where "+SCRIPTS.DEPENDENCE_ID_REQUIREMENT+"="+id_requirement+
                            " and "+SCRIPTS.DEPENDENCE_ID_PROJECT+"="+id_project+";", null);
*/

        if(c.getCount()>0){
            c.moveToFirst();
            do{
                dependencies.add(setDependent(c, id_requirement, id_project));
            }while (c.moveToNext());
        }
        c.close();

        return dependencies;
    }


    private Dependence setDependent(Cursor c, String id_requirement,String id_project){
/*        String id_requirement = ""+c.getInt(c.getColumnIndex(SCRIPTS.DEPENDENCE_ID_REQUIREMENT));
        String id_project = ""+c.getInt(c.getColumnIndex(SCRIPTS.DEPENDENCE_ID_PROJECT));*/
        String id_dependent = ""+c.getInt(c.getColumnIndex(SCRIPTS.DEPENDENCE_ID_DEPENDENT));

        Dependence nDependence = new Dependence(id_requirement, id_project, id_dependent);

        return nDependence;
    }


    public void insertDependences(ArrayList<Dependence> newDependents) {
        if(newDependents.size()>0){
            for(Dependence dependence:newDependents){
                insertDependence(dependence);
            }
        }
    }

    public void removeDependeces(ArrayList<Dependence> removedDependets) {
        if(removedDependets.size()>0){
            for(Dependence dependence : removedDependets){
                removeDependence(dependence);
            }
        }
    }

    private void removeDependence(Dependence dependence) {

        mDB.delete(SCRIPTS.TABLE_DEPENDENCE,SCRIPTS.DEPENDENCE_ID_REQUIREMENT+"="+dependence.getId_requirement()+" AND "+
                                            SCRIPTS.DEPENDENCE_ID_PROJECT+"="+dependence.getId_project()+ " AND "+
                                            SCRIPTS.DEPENDENCE_ID_DEPENDENT+"="+dependence.getId_dependent(),null);
    }
}
