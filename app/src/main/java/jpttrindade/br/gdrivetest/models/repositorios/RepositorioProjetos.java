package jpttrindade.br.gdrivetest.models.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import jpttrindade.br.gdrivetest.models.Projeto;

/**
 * Created by jpttrindade on 07/08/14.
 */
public class RepositorioProjetos {

    private Context mContext;
    private static RepositorioProjetos singleton;
    private SQLiteDatabase mDB;

    private RepositorioProjetos(Context ctx){
        mContext = ctx.getApplicationContext();
        mDB =  SQLiteBDHelper.getInstance(mContext).getWritableDatabase();
    }

    public static RepositorioProjetos getInstance(Context ctx){
        if(singleton == null){
            singleton = new RepositorioProjetos(ctx);
        }

        return singleton;
    }

    public long insertProjeto(Projeto nProjeto){
        ContentValues values = new ContentValues();
        values.put(SCRIPTS.PROJECT_TITLE, nProjeto.getTitulo());
        values.put(SCRIPTS.PROJECT_DESCRIPTION, nProjeto.getDescricao());
        values.put(SCRIPTS.PROJECT_MANAGER, nProjeto.getGerente());
        values.put(SCRIPTS.PROJECT_LASTREQUIREMENT, 1);

        long id = mDB.insert(SCRIPTS.TABLE_PROJECT, null, values);

        nProjeto.setId("" + id);

        return id;
    }

    public ArrayList<Projeto> getProjetos() {
        ArrayList<Projeto> projetos = new ArrayList<Projeto>();

        String query = new QUERY().select().from(SCRIPTS.TABLE_PROJECT).finish();
        Cursor c = mDB.rawQuery(query, null);
        //Cursor c = mDB.query(SCRIPTS.TABLE_PROJECT, new String[]{}, null, new String[]{}, null, null, null, null);

        if(c.getCount()>0) {
            c.moveToFirst();

            do {
                String id = c.getString(c.getColumnIndex(SCRIPTS.PROJECT_ID));
                String titulo = c.getString(c.getColumnIndex(SCRIPTS.PROJECT_TITLE));
                String descricao = c.getString(c.getColumnIndex(SCRIPTS.PROJECT_DESCRIPTION));
                String gerente = c.getString(c.getColumnIndex(SCRIPTS.PROJECT_MANAGER));
                int idNextRequirement = c.getInt(c.getColumnIndex(SCRIPTS.PROJECT_LASTREQUIREMENT));
                projetos.add(new Projeto(id, titulo, descricao, gerente, idNextRequirement));

            } while (c.moveToNext());
        }
        c.close();

        return projetos;
    }

    public int updateProjeto(Projeto projeto){

        ContentValues values = new ContentValues();

        values.put(SCRIPTS.PROJECT_TITLE, projeto.getTitulo());
        values.put(SCRIPTS.PROJECT_DESCRIPTION, projeto.getDescricao());
        values.put(SCRIPTS.PROJECT_MANAGER, projeto.getGerente());
        values.put(SCRIPTS.PROJECT_LASTREQUIREMENT, projeto.getIdNextRequirements());

        return mDB.update(SCRIPTS.TABLE_PROJECT, values, SCRIPTS.PROJECT_ID+"="+projeto.getId(), null);

    }
}

