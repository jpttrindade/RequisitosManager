package jpttrindade.br.gdrivetest.models.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.RequerimentType;
import jpttrindade.br.gdrivetest.models.RequirementStatus;
import jpttrindade.br.gdrivetest.models.Requisito;

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
        String status = nRequirement.getStatus().toString();
        String type = nRequirement.getType().toString();
        String dataCriacao = ""+nRequirement.getDataCriacao().getTime();
        String dataModificacao = ""+nRequirement.getDataModificacao().getTime();
        String requerente = nRequirement.getRequerente();
        String id_projeto = nRequirement.getProjeto().getId();
        //ArrayList<Requisito> dependentes;

        values.put(SCRIPTS.REQUIREMENT_TITLE, titulo);
        values.put(SCRIPTS.REQUIREMENT_ID, id);
        values.put(SCRIPTS.REQUIREMENT_DESCRIPTION, descricao);
        values.put(SCRIPTS.REQUIREMENT_STATUS, status);
        values.put(SCRIPTS.REQUIREMENT_TYPE, type);
        values.put(SCRIPTS.REQUIREMENT_DATE_CREATION, dataCriacao);
        values.put(SCRIPTS.REQUIREMENT_DATE_MODIFICATION, dataModificacao);
        values.put(SCRIPTS.REQUIREMENT_REQUESTER, requerente);
        values.put(SCRIPTS.REQUIREMENT_PROJECT_ID, id_projeto);

        RepositorioDependences repoDependents = RepositorioDependences.getInstance(mContext);

        for(Requisito dependent : nRequirement.getDependentes()){
            long newID = repoDependents.addDependence(nRequirement,dependent);
            Log.d("DEBUG", "id_dependent --- "+newID);
        }

        return mDB.insert(SCRIPTS.TABLE_REQUIREMENTS, null, values);
    }

    public ArrayList<Requisito> getRequisitos(Projeto projeto) throws ParseException {
        ArrayList<Requisito> requisitos = new ArrayList<Requisito>();
        //String query = "select * from " + SCRIPTS.TABLE_REQUIREMENTS+ " where "+SCRIPTS.PROJECT_ID+"=" + projeto.getId() + ";";

        String query = new QUERY().select().from(SCRIPTS.TABLE_REQUIREMENTS)
                                    .where().whereClause(SCRIPTS.PROJECT_ID,projeto.getId())
                                    .finish();

        //Log.d("DEBUG", query);

        Cursor c = mDB.rawQuery(query, null);

        //Log.d("DEBUG", "cur = "+c.getCount());

        if(c.getCount()>0) {
            c.moveToFirst();

            do {
                String titulo = c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_TITLE));
                String id = c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_ID));
                String descricao = c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_DESCRIPTION));

                RequirementStatus status = RequirementStatus
                        .setStatus(c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_STATUS)));

                RequerimentType type = RequerimentType
                        .setType(c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_TYPE)));
                Date dataCriacao = new Date(Long.parseLong(c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_DATE_CREATION))));
                Date dataModificacao = new Date(Long.parseLong(c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_DATE_MODIFICATION))));
                String requerente = c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_REQUESTER));
                Projeto proj = projeto;
                ArrayList<Requisito> dependentes = new ArrayList<Requisito>();

                ArrayList<String> ids_dependents = RepositorioDependences.getInstance(mContext).getDependents(id, projeto.getId());

                dependentes.addAll(getRequisitos(projeto, ids_dependents));

                requisitos.add(new Requisito(titulo, id, descricao, status,type, dataCriacao, dataModificacao, requerente, proj, dependentes));

            } while(c.moveToNext());
        }

        c.close();
        //Log.d("DEBUG", "reqs = "+requisitos.size());

        return requisitos;
    }


    public ArrayList<Requisito> getRequisitos(Projeto projeto, ArrayList<String> ids){
        ArrayList<Requisito> requirements = new ArrayList<Requisito>();


        QUERY query = new QUERY().select().from(SCRIPTS.TABLE_REQUIREMENTS)
                .where().whereClause(SCRIPTS.REQUIREMENT_PROJECT_ID, projeto.getId());

        if(ids.size()>0){
            query.and().openComplexClause();
        }

        for(int i=0; i<ids.size(); i++){
            Log.d("DEBUG",SCRIPTS.REQUIREMENT_ID+ "="+ids.get(i));
            query.whereClause(SCRIPTS.REQUIREMENT_ID, ids.get(i));
            if(i!=ids.size()-1) {
                query.or();
            }
        }


        if(ids.size()>0){
            query.closeComplexClause();
        }
        String quer = query.finish();

        Log.d("DEBUG", "QUERY >>>> "+quer);

        Cursor c = mDB.rawQuery(quer, null);

        if(c.getCount() >0){
            c.moveToFirst();

            do{
                String titulo = c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_TITLE));
                String id = c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_ID));
                String descricao = c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_DESCRIPTION));

                RequirementStatus status = RequirementStatus
                        .setStatus(c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_STATUS)));

                RequerimentType type = RequerimentType
                        .setType(c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_TYPE)));
                Date dataCriacao = new Date(Long.parseLong(c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_DATE_CREATION))));
                Date dataModificacao = new Date(Long.parseLong(c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_DATE_MODIFICATION))));
                String requerente = c.getString(c.getColumnIndex(SCRIPTS.REQUIREMENT_REQUESTER));
                Projeto proj = projeto;

                requirements.add(new Requisito(titulo, id, descricao, status,type, dataCriacao, dataModificacao, requerente, proj));


            }while (c.moveToNext());
        }

        c.close();

        return requirements;
    }
}
