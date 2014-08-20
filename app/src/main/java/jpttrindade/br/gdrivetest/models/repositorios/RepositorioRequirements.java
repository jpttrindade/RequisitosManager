package jpttrindade.br.gdrivetest.models.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import jpttrindade.br.gdrivetest.models.Dependence;
import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.RequerimentType;
import jpttrindade.br.gdrivetest.models.Requirement;
import jpttrindade.br.gdrivetest.models.RequirementStatus;

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

    public long insertRequirement(Requirement nRequirement){

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


        values.put(SCRIPTS.REQUIREMENT_TITLE, titulo);
        values.put(SCRIPTS.REQUIREMENT_ID, id);
        values.put(SCRIPTS.REQUIREMENT_DESCRIPTION, descricao);
        values.put(SCRIPTS.REQUIREMENT_STATUS, status);
        values.put(SCRIPTS.REQUIREMENT_TYPE, type);
        values.put(SCRIPTS.REQUIREMENT_DATE_CREATION, dataCriacao);
        values.put(SCRIPTS.REQUIREMENT_DATE_MODIFICATION, dataModificacao);
        values.put(SCRIPTS.REQUIREMENT_REQUESTER, requerente);
        values.put(SCRIPTS.REQUIREMENT_PROJECT_ID, id_projeto);

        RepositorioDependences.getInstance(mContext).insertDependences(nRequirement.getDependences(), nRequirement.getId());



        return mDB.insert(SCRIPTS.TABLE_REQUIREMENTS, null, values);
    }

    public ArrayList<Requirement> getRequisitos(Projeto projeto) throws ParseException {
        ArrayList<Requirement> requirements = new ArrayList<Requirement>();

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

                ArrayList<Dependence> dependences = RepositorioDependences.getInstance(mContext).getDependencies(id, projeto.getId());


                requirements.add(new Requirement(titulo, id, descricao, status,type, dataCriacao, dataModificacao, requerente, proj, dependences));

            } while(c.moveToNext());
        }

        c.close();
        //Log.d("DEBUG", "reqs = "+requisitos.size());

        return requirements;
    }


    public ArrayList<Requirement> getRequisitos(Projeto projeto, ArrayList<String> ids){
        ArrayList<Requirement> requirements = new ArrayList<Requirement>();


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

                requirements.add(new Requirement(titulo, id, descricao, status,type, dataCriacao, dataModificacao, requerente, proj));


            }while (c.moveToNext());
        }

        c.close();

        return requirements;
    }

    public int updateRequirement(Requirement requirement){
        ContentValues values = new ContentValues();

        values.put(SCRIPTS.REQUIREMENT_TITLE, requirement.getTitulo());
        values.put(SCRIPTS.REQUIREMENT_DESCRIPTION, requirement.getDescricao());
        values.put(SCRIPTS.REQUIREMENT_REQUESTER, requirement.getRequerente());
        values.put(SCRIPTS.REQUIREMENT_STATUS, requirement.getStatus().toString());
        values.put(SCRIPTS.REQUIREMENT_DATE_MODIFICATION, requirement.getDataModificacao().getTime());
        values.put(SCRIPTS.REQUIREMENT_TYPE, requirement.getType().toString());


        RepositorioDependences.getInstance(mContext).insertDependences(requirement.getNewDependents(), null);

        RepositorioDependences.getInstance(mContext).removeDependeces(requirement.getRemovedDependets());

        return mDB.update(SCRIPTS.TABLE_REQUIREMENTS, values, SCRIPTS.REQUIREMENT_ID+"="+ requirement.getId() +
                          " AND " +
                          SCRIPTS.REQUIREMENT_PROJECT_ID+"="+ requirement.getProjeto().getId(), null);

    }

    public void deleteRequirement(Requirement requirement) {
        mDB.delete(SCRIPTS.TABLE_REQUIREMENTS, SCRIPTS.REQUIREMENT_ID+"="+requirement.getId()+
                                               " AND " +
                                               SCRIPTS.REQUIREMENT_PROJECT_ID+"="+requirement.getProjeto().getId(), null);

        RepositorioDependences.getInstance(mContext).deleteDependences(requirement.getDependences());

    }
}

