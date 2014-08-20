package jpttrindade.br.gdrivetest.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.Dependence;
import jpttrindade.br.gdrivetest.models.RequerimentType;
import jpttrindade.br.gdrivetest.models.Requirement;
import jpttrindade.br.gdrivetest.models.RequirementStatus;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioRequirements;

/**
 * Created by joaotrindade on 13/08/14.
 */
public class EditRequirementActivity extends Activity{


    EditText description;
    EditText title;

    Spinner spinner_status, spinner_type;

    ListView lv_dependents;

    Button bt_save, bt_cancel, bt_delete;

    RequirementStatus status;
    RequerimentType type;





    private Requirement requirement;
    private ArrayList<Requirement> requirements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_requirement);

        initialize();

        title.setText(requirement.getTitulo());
        description.setText(requirement.getDescricao());

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent();
                it.putExtra("requirement", requirement);
                setResult(RESULT_CANCELED, it);
                finish();
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requirement.setTitulo(title.getText().toString());
                requirement.setDescricao(description.getText().toString());
                requirement.setStatus(status);
                requirement.setType(type);
                requirement.setDataModificacao(Calendar.getInstance().getTime());

                RepositorioRequirements.getInstance(EditRequirementActivity.this)
                                        .updateRequirement(requirement);

                Intent it = new Intent();


                it.putExtra("requirement", requirement);


                setResult(RESULT_OK, it);
                finish();
            }
        });

        bt_delete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(EditRequirementActivity.this).setTitle("Confimation")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RepositorioRequirements.getInstance(EditRequirementActivity.this)
                                                        .deleteRequirement(requirement);
                                Intent it = new Intent();


                                it.putExtra("id_reqDeleted", requirement.getId());

                                setResult(RequirementsActivity.RESULT_REQUIREMENT_DELETED, it);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                            }
                        }).show();
            }
        });

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = RequerimentType.setType(((TextView)view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = RequirementStatus.setStatus(((TextView)view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        switch (requirement.getStatus()){

            case ABERTO:
                spinner_status.setSelection(0);
                break;
            case ACORDADO:
                spinner_status.setSelection(1);
                break;

            case VALIDACAO:
                spinner_status.setSelection(2);
                break;
            case CONCLUIDO:
                spinner_status.setSelection(3);
                break;

            case STANDBY:
                spinner_status.setSelection(4);
                break;

        }

        switch (requirement.getType()){

            case RF:
                spinner_type.setSelection(0);
                break;
            case RNF:
                spinner_type.setSelection(1);
                break;
        }


    }

    private void initialize() {
        requirement = (Requirement)getIntent().getParcelableExtra("requirement");
        requirements = getIntent().getParcelableArrayListExtra("requirements");

        Log.d("DEBUG", "REQUISITO REMOVIDO DA LISTA -- "+requirements.remove(requirement));

        title = (EditText) findViewById(R.id.et_title);
        description = (EditText) findViewById(R.id.et_description);

        spinner_status = (Spinner) findViewById(R.id.spinner_status);
        spinner_type= (Spinner) findViewById(R.id.spinner_type);

        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);

        adapterStatus.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner_status.setAdapter(adapterStatus);

        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner_type.setAdapter(adapterType);

        lv_dependents = (ListView) findViewById(R.id.lv_dependents);

        lv_dependents.setAdapter(new DependencesAdapter());

        bt_save = (Button) findViewById(R.id.bt_save);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_delete = (Button) findViewById(R.id.bt_delete);

    }

    private class DependencesAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return requirements.size();
        }

        @Override
        public Object getItem(int position) {
            return requirements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CheckBox checkBox =(CheckBox) LayoutInflater.from(EditRequirementActivity.this).inflate(R.layout.checkbox_dependents, null);

            Requirement req = requirements.get(position);

            checkBox.setText(req.getTitulo());

            Dependence dependence = new Dependence(req.getId(), requirement.getProjeto().getId(), requirement.getId());

            dependence.setTitle(req.getTitulo());
            dependence.setDescription(req.getDescricao());

            checkBox.setTag(dependence);

            for(Dependence depen : requirement.getDependences()){
                if(depen.getId_parent().equals(req.getId())){
                    checkBox.setChecked(true);
                }
            }

            checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        requirement.addNewDependence((Dependence) buttonView.getTag());
                    } else {
                        requirement.addRemovedDependence((Dependence) buttonView.getTag());
                    }
                }
            });




            return checkBox;
        }
    }
}
