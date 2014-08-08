package jpttrindade.br.gdrivetest.models.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.SQLiteBDHelper;

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

    public long addProjeto(Projeto nProjeto){
        ContentValues values = new ContentValues();
        values.put(SQLiteBDHelper.PROJETO_TITULO, nProjeto.getTitulo());
        values.put(SQLiteBDHelper.PROJETO_DESCRICAO, nProjeto.getDescricao());
        values.put(SQLiteBDHelper.PROJETO_GERENTE, nProjeto.getGerente());

        long id = mDB.insert(SQLiteBDHelper.TABLE_PROJETO, null, values);

        nProjeto.setId("" + id);

        return id;
    }

    public ArrayList<Projeto> getProjetos() {
        ArrayList<Projeto> projetos = new ArrayList<Projeto>();

        Cursor c = mDB.query(SQLiteBDHelper.TABLE_PROJETO, new String[]{}, null, new String[]{}, null, null, null, null);
        if(c.getCount()>0) {
            c.moveToFirst();

            do {
                String id = c.getString(c.getColumnIndex(SQLiteBDHelper.PROJETO_ID));
                String titulo = c.getString(c.getColumnIndex(SQLiteBDHelper.PROJETO_TITULO));
                String descricao = c.getString(c.getColumnIndex(SQLiteBDHelper.PROJETO_DESCRICAO));
                String gerente = c.getString(c.getColumnIndex(SQLiteBDHelper.PROJETO_GERENTE));
                int idNextRequirement = c.getInt(c.getColumnIndex(SQLiteBDHelper.PROJETO_TOTAL_REQUISITOS));
                projetos.add(new Projeto(id, titulo, descricao, gerente, idNextRequirement));

            } while (c.moveToNext());
        }
        return projetos;
    }

    public int updateProjeto(Projeto projeto){

        ContentValues values = new ContentValues();

        values.put(SQLiteBDHelper.PROJETO_TITULO, projeto.getTitulo());
        values.put(SQLiteBDHelper.PROJETO_DESCRICAO, projeto.getDescricao());
        values.put(SQLiteBDHelper.PROJETO_GERENTE, projeto.getGerente());
        values.put(SQLiteBDHelper.PROJETO_TOTAL_REQUISITOS, projeto.getIdNextRequirements());

        return mDB.update(SQLiteBDHelper.TABLE_PROJETO, values, SQLiteBDHelper.PROJETO_ID+"="+projeto.getId(), null);

    }
}

