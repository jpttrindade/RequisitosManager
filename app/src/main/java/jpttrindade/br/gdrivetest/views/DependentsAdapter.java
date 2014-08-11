package jpttrindade.br.gdrivetest.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.Requisito;

/**
 * Created by jpttrindade on 10/08/14.
 */
public class DependentsAdapter extends BaseAdapter {

    private ArrayList<Requisito> dependents;
     private Context mContext;

    public DependentsAdapter(Context context, ArrayList<Requisito> dependents){
        mContext = context;
        this.dependents = dependents;
    }

    @Override
    public int getCount() {
        return dependents.size();
    }

    @Override
    public Object getItem(int position) {
        return dependents.get(position);
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
        Requisito req = dependents.get(position);

        tv_id.setText(req.getId());
        tv_title.setText(req.getTitulo());
        return convertView;
    }
}
