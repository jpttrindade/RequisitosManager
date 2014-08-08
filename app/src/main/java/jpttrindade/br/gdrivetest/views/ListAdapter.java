package jpttrindade.br.gdrivetest.views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.ItemMenu;
import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioProjetos;
import jpttrindade.br.gdrivetest.models.SubItemMenu;

/**
 * Created by joaotrindade on 05/08/14.
 */
public class ListAdapter extends BaseExpandableListAdapter {

    public static final int OPTION_MENU_PROJETOS = 0;

    private Context mContext;
    ArrayList<ItemMenu> menu;


    public ListAdapter(Context context){

        mContext = context;

        povoarMenu();
    }

    private void povoarMenu() {
        menu  = new ArrayList<ItemMenu>();
        ItemMenu projetos = new ItemMenu("Projetos");

        menu.add(projetos);

        new PovoarProjetosAsyncTask().execute(projetos);
    }


    @Override
    public int getGroupCount() {
        return menu.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return menu.get(groupPosition).getSubItens().size();
    }

    @Override
    public ItemMenu getGroup(int groupPosition) {
        return menu.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return menu.get(groupPosition).getSubItens().get(childPosition).getTitulo();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        int color;
        switch (groupPosition){
            case 0:
                  color = android.R.color.holo_green_light;
                break;
            case 1:
                color = android.R.color.holo_orange_light;
                break;
            case 2:
                color = android.R.color.holo_blue_light;
                break;
            case 3:
                color = android.R.color.holo_red_light;
                break;
            case 4:
                color = android.R.color.holo_purple;
                break;
            default:
                color = android.R.color.holo_green_light;
        }
        convertView = View.inflate(mContext, R.layout.item_menu, null);

        TextView membro = (TextView)convertView.findViewById(R.id.tv_title);
        membro.setText(menu.get(groupPosition).getNome());
       // membro.setBackgroundColor(mContext.getResources().getColor(color));
        convertView.setBackgroundColor(mContext.getResources().getColor(color));
      //  convertView.setBackgroundResource(mContext.getResources().getColor(color));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.subitem_menu, null);

        ((TextView)convertView.findViewById(R.id.tv_title)).setText(menu.get(groupPosition).getSubItens().get(childPosition).getTitulo());
        convertView.setTag(menu.get(groupPosition).getSubItens().get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    public void addSubItemMenu(int menuItem, SubItemMenu subItemMenu){
        menu.get(menuItem).addSubItem(subItemMenu);
        notifyDataSetChanged();
    }

    public void attProjetos() {
       povoarMenu();
    }


    private class PovoarProjetosAsyncTask extends AsyncTask<ItemMenu, Void, Void>{

        @Override
        protected Void doInBackground(ItemMenu... itensMenu) {

           ArrayList<Projeto> projetos = RepositorioProjetos.getInstance(mContext).getProjetos();
           for(Projeto p:projetos) {
                itensMenu[0].addSubItem(p);
                publishProgress();
           }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            notifyDataSetChanged();
        }
    }


}
