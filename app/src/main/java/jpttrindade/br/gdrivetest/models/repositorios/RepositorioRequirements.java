package jpttrindade.br.gdrivetest.models.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.Requisito;
import jpttrindade.br.gdrivetest.models.SQLiteBDHelper;
import jpttrindade.br.gdrivetest.models.Status;

/**
 * Created by jpttrindade on 07/08/14.
 */
public class RepositorioRequirements {

    private static RepositorioRequirements singleton;
    private SQLiteDatabase mDB;
    private Context mContext;

    private RepositorioRequirements(Context context){
        mContext = context.getApplicationContext();
        mDB =  SQLiteBDHelper.getInstance(mContext).getWritableDatabase();
    }

    public static RepositorioRequirements getInstance(Context context){
        if(singleton == null){
            singleton = new RepositorioRequirements(context);
        }
        return singleton;
    }

    public long addRequirement(Requisito nRequirement){

        ContentValues values = new ContentValues();

        String titulo = nRequirement.getTitulo();
        String id = nRequirement.getId();
        String descricao = nRequirement.getDescricao();
        String status = nRequirement.getStatus().getStatus();
        String dataCriacao = ""+nRequirement.getDataCriacao().getTime();
        String dataModificacao = ""+nRequirement.getDataModificacao().getTime();
        String requerente = nRequirement.getRequerente();
        String id_projeto = nRequirement.getProjeto().getId();
        //ArrayList<Requisito> dependentes;

        values.put(SQLiteBDHelper.REQUISITOS_TITULO, titulo);
        values.put(SQLiteBDHelper.REQUISITOS_ID, id);
        values.put(SQLiteBDHelper.REQUISITOS_DESCRICAO, descricao);
        values.put(SQLiteBDHelper.REQUISITOS_STATUS, status);
        values.put(SQLiteBDHelper.REQUISITOS_DATACRIACAO, dataCriacao);
        values.put(SQLiteBDHelper.REQUISITOS_DATAMODIFICACAO, dataModificacao);
        values.put(SQLiteBDHelper.REQUISITOS_REQUERENTE, requerente);
        values.put(SQLiteBDHelper.REQUISITOS_ID_PROJETO, id_projeto);

        return mDB.insert(SQLiteBDHelper.TABLE_REQUISITOS, null, values);
    }

    public ArrayList<Requisito> getRequisitos(Projeto projeto) throws ParseException {
       ArrayList<Requisito> requisitos = new ArrayList<Requisito>();
        String query = "select * from " + SQLiteBDHelper.TABLE_REQUISITOS + " where tb_projeto_id_projeto=" + projeto.getId() + ";";

        //Log.d("DEBUG", query);

        Cursor c = mDB.rawQuery(query, null);

        Log.d("DEBUG", "cur = "+c.getCount());

        if(c.getCount()>0) {
            c.moveToFirst();
            boolean parse = false;
            do {
                String titulo = c.getString(c.getColumnIndex(SQLiteBDHelper.REQUISITOS_TITULO));
                String id = c.getString(c.getColumnIndex(SQLiteBDHelper.REQUISITOS_ID));
                String descricao = c.getString(c.getColumnIndex(SQLiteBDHelper.REQUISITOS_DESCRICAO));

                String sts = c.getString(c.getColumnIndex(SQLiteBDHelper.REQUISITOS_STATUS));
                Status status = (sts.equals(Status.ABERTO.getStatus()) ? Status.ABERTO :
                        (sts.equals(Status.CONCLUIDO.getStatus()) ? Status.CONCLUIDO :
                                Status.STANDBY));


                Date dataCriacao = new Date(Long.parseLong(c.getString(c.getColumnIndex(SQLiteBDHelper.REQUISITOS_DATACRIACAO))));
                Date dataModificacao = new Date(Long.parseLong(c.getString(c.getColumnIndex(SQLiteBDHelper.REQUISITOS_DATAMODIFICACAO))));
                String requerente = c.getString(c.getColumnIndex(SQLiteBDHelper.REQUISITOS_REQUERENTE));
                Projeto proj = projeto;
                ArrayList<Requisito> dependentes = new ArrayList<Requisito>();

                requisitos.add(new Requisito(titulo, id, descricao, status, dataCriacao, dataModificacao, requerente, proj, dependentes));
                 parse = c.moveToNext();
                Log.d("DEBUG", "parse = "+parse);
            } while(parse);
        }

        Log.d("DEBUG", "reqs = "+requisitos.size());

        return requisitos;
    }

}
