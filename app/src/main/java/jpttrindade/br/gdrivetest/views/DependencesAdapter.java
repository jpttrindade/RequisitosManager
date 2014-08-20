package jpttrindade.br.gdrivetest.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.Dependence;

/**
 * Created by jpttrindade on 10/08/14.
 */
public class DependencesAdapter extends BaseAdapter {

    private ArrayList<Dependence> dependencies;
     private Context mContext;

    public DependencesAdapter(Context context, ArrayList<Dependence> dependencies){
        mContext = context;
        this.dependencies = dependencies;
    }

    @Override
    public int getCount() {
        return dependencies.size();
    }

    @Override
    public Object getItem(int position) {
        return dependencies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.dependents_list, null);

        TextView tv_id = (TextView) convertView.findViewById(R.id.tv_id);
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        Dependence dependence = dependencies.get(position);

        tv_id.setText(dependence.getId_parent());
        tv_title.setText(dependence.getDescription());
        return convertView;
    }
}
