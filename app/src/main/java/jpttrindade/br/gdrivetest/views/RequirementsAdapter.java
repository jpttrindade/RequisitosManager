package jpttrindade.br.gdrivetest.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.SubItemMenu;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioRequirements;
import jpttrindade.br.gdrivetest.models.Requisito;

/**
 * Created by jpttrindade on 07/08/14.
 */
public class RequirementsAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private ArrayList<Requisito> requirements;
    private Projeto projeto;

    public RequirementsAdapter(Context ctx, Projeto projeto){
        mContext = ctx;
        this.projeto = projeto;
        requirements = new ArrayList<Requisito>();

        new GetRequirementsAsyncTask().execute();
    }

    @Override
    public int getGroupCount() {
        return requirements.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Requisito getGroup(int groupPosition) {
        return requirements.get(groupPosition);
    }

    @Override
    public Requisito getChild(int groupPosition, int childPosition) {
        return requirements.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.requirement_item_menu, null);
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tv_id = (TextView) convertView.findViewById(R.id.tv_id);
        TextView tv_status = (TextView) convertView.findViewById(R.id.tv_status);
        TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);

        Requisito req = requirements.get(groupPosition);
        tv_title.setText(req.getTitulo());
        tv_id.setText(req.getId());
        tv_status.setText(req.getStatus().toString());
        tv_type.setText(req.getType().toString());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.desc_requirement, parent, false);
     //   TextView id = (TextView) convertView.findViewById(R.id.tv_id);
     //   TextView status = (TextView) convertView.findViewById(R.id.tv_status);
        TextView descricao = (TextView) convertView.findViewById(R.id.tv_descricao);
        TextView requerente = (TextView) convertView.findViewById(R.id.tv_requerente);
        TextView criacao = (TextView) convertView.findViewById(R.id.tv_criacao);
        TextView modificacao = (TextView) convertView.findViewById(R.id.tv_modificacao);

        ListView lv_dependents = (ListView) convertView.findViewById(R.id.lv_dependents);

        Requisito req = requirements.get(groupPosition);

        DependentsAdapter dependentsAdapter = new DependentsAdapter(mContext, req.getDependentes());
        lv_dependents.setAdapter(dependentsAdapter);

     //   id.setText(req.getId());
     //   status.setText(req.getStatus().toString());
        descricao.setText(req.getDescricao());
        requerente.setText(req.getRequerente());
        criacao.setText(req.getDataCriacao().toString());
        modificacao.setText(req.getDataModificacao().toString());

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }



    public void addRequirement(Requisito nReq){
        requirements.add(nReq);
        notifyDataSetChanged();
    }

    private class GetRequirementsAsyncTask extends AsyncTask <Void, Void, Void> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  dialog = ProgressDialog.show(mContext, "","wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
           RepositorioRequirements repositorioRequirements =  RepositorioRequirements.getInstance(mContext);


            ArrayList<Requisito> query = null;
            try {
                query = repositorioRequirements.getRequisitos(projeto);
                requirements.addAll(query);
                publishProgress();

            } catch (ParseException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
}
