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

        values.put(SCRIPTS.DEPENDENCE_ID_PARENT, dependence.getId_parent());
        values.put(SCRIPTS.DEPENDENCE_ID_PROJECT, dependence.getId_project());
        values.put(SCRIPTS.DEPENDENCE_ID_CHILD, dependence.getId_child());

        return mDB.insert(SCRIPTS.TABLE_DEPENDENCE,null, values);
    }

    public ArrayList<Dependence> getDependencies(String id_child, String id_project) {


        ArrayList<Dependence> dependencies = new ArrayList<Dependence>();

        String query = new QUERY().select(SCRIPTS.DEPENDENCE_ID_PARENT).from(SCRIPTS.TABLE_DEPENDENCE)
                                    .where().whereClause(SCRIPTS.DEPENDENCE_ID_CHILD, id_child)
                                    .and().whereClause(SCRIPTS.DEPENDENCE_ID_PROJECT, id_project)
                                    .finish();

        Cursor c = mDB.rawQuery(query, null);


        if(c.getCount()>0){
            c.moveToFirst();
            do{
                    dependencies.add(setDependent(c, id_child, id_project));
            }while (c.moveToNext());
        }
        c.close();

        return dependencies;
    }


    private Dependence setDependent(Cursor c, String id_child,String id_project){
       String id_parent = ""+c.getInt(c.getColumnIndex(SCRIPTS.DEPENDENCE_ID_PARENT));

        Dependence nDependence = new Dependence(id_parent, id_project, id_child);

        return nDependence;
    }


    public void insertDependences(ArrayList<Dependence> newDependents, String id_child) {
        if(newDependents.size()>0){
            for(Dependence dependence:newDependents){
                if(id_child != null){
                    dependence.setId_child(id_child);
                }
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
        mDB.delete(SCRIPTS.TABLE_DEPENDENCE,SCRIPTS.DEPENDENCE_ID_PARENT +"="+dependence.getId_parent()+" AND "+
                                            SCRIPTS.DEPENDENCE_ID_PROJECT+"="+dependence.getId_project()+ " AND "+
                                            SCRIPTS.DEPENDENCE_ID_CHILD +"="+dependence.getId_child(),null);
    }

    public void deleteDependences(ArrayList<Dependence> dependentes) {
        for(Dependence dependence : dependentes){
            deleteDependence(dependence);
        }
    }

    private void deleteDependence(Dependence dependence) {
        /*mDB.delete(SCRIPTS.TABLE_DEPENDENCE, SCRIPTS.DEPENDENCE_ID_PROJECT+"="+dependence.getId_project()+
                                            " AND (" +
                                             SCRIPTS.DEPENDENCE_ID_PARENT +"="+dependence.getId_parent()+
                                             " OR " +
                                             SCRIPTS.DEPENDENCE_ID_PARENT +"="+dependence.getId_child()+
                                             ") AND (" +
                                             SCRIPTS.DEPENDENCE_ID_CHILD +"="+dependence.getId_child()+
                                             " OR " +
                                             SCRIPTS.DEPENDENCE_ID_CHILD +"="+dependence.getId_parent()+")"
                                             , null);

*/
        mDB.delete(SCRIPTS.TABLE_DEPENDENCE,
                   SCRIPTS.DEPENDENCE_ID_PROJECT+"="+dependence.getId_project()+
                   " AND " +
                   SCRIPTS.DEPENDENCE_ID_PARENT+"="+dependence.getId_parent()+
                   " AND " +
                   SCRIPTS.DEPENDENCE_ID_CHILD+"="+dependence.getId_child(), null);

        mDB.delete(SCRIPTS.TABLE_DEPENDENCE,

                SCRIPTS.DEPENDENCE_ID_PROJECT+"="+dependence.getId_project()+
                        " AND " +
                SCRIPTS.DEPENDENCE_ID_PARENT+"="+dependence.getId_child()
                ,null
        );
    }
}
