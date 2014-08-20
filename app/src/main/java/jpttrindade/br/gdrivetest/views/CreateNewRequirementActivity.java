package jpttrindade.br.gdrivetest.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.Dependence;
import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.RequerimentType;
import jpttrindade.br.gdrivetest.models.Requirement;
import jpttrindade.br.gdrivetest.models.RequirementStatus;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioRequirements;

/**
 * Created by jpttrindade on 08/08/14.
 */
public class CreateNewRequirementActivity  extends Activity{

    private Spinner spinnerStatus, spinnerType;
    private Button create, cancel;

    private TextView tv_titulo, tv_descricao, tv_requerente;

    private LinearLayout ll_container;

    private Date dataCricacao;
    private RequirementStatus status;
    private RequerimentType type;

    private Projeto projeto;

    private String titulo, descricao, requerente;
    private ArrayList<Requirement> requirements;
    private ArrayList<Dependence> dependences;

    private void initialize() {
        requerente = descricao = titulo = "";

        projeto = (Projeto) getIntent().getParcelableExtra("projeto");

        create = (Button) findViewById(R.id.bt_create);
        cancel = (Button) findViewById(R.id.bt_cancel);

        tv_titulo = (TextView) findViewById(R.id.et_titulo);
        tv_descricao = (TextView) findViewById(R.id.et_descricao);
        tv_requerente = (TextView) findViewById(R.id.et_requerente);

        spinnerStatus = (Spinner) findViewById(R.id.spinner_status);
        spinnerType = (Spinner) findViewById(R.id.spinner_type);

        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);

        adapterStatus.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinnerStatus.setAdapter(adapterStatus);

        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinnerType.setAdapter(adapterType);

        requirements = getIntent().getParcelableArrayListExtra("requirements");

        dependences = new ArrayList<Dependence>();
        ll_container = (LinearLayout) findViewById(R.id.ll_container);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewrequirement);


        initialize();

       // new DependetsAsyncTask().execute(projeto);

        setDependences();

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              status = RequirementStatus.setStatus(((TextView)view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = RequerimentType.setType(((TextView)view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid()) {
                    dataCricacao = Calendar.getInstance(TimeZone.getTimeZone("America/Recife")).getTime();
                    String id = projeto.setIdNextRequirements() + "";

                    Requirement nRequirement = new Requirement(titulo, id, descricao, status, type, dataCricacao, dataCricacao,
                            requerente, projeto, dependences);

                    RepositorioRequirements.getInstance(CreateNewRequirementActivity.this).insertRequirement(nRequirement);


                    Intent it = new Intent();
                    it.putExtra("requirement", nRequirement);
                    it.putExtra("projeto", projeto);
                    setResult(RESULT_OK, it);
                    finish();
                }else{
                    Toast.makeText(CreateNewRequirementActivity.this, "Preencha todos os dados!", Toast.LENGTH_LONG).show();
                }
            }

        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });



    }


    private boolean isValid() {
        boolean valid = false;

        titulo = tv_titulo.getText().toString();
        descricao = tv_descricao.getText().toString();
        requerente = tv_requerente.getText().toString();

        if(titulo.isEmpty()){
            tv_titulo.requestFocus();
        } else if( descricao.isEmpty()){
                tv_descricao.requestFocus();
        }else  if(requerente.isEmpty()){
            tv_requerente.requestFocus();
        } else {
            valid = true;
        }

        return valid;
    }

    private void setDependences() {
        LayoutInflater inflater = LayoutInflater.from(CreateNewRequirementActivity.this);
        CheckBox checkbox = null;
        Dependence dependence;

        for(Requirement req : requirements){
            checkbox = (CheckBox) inflater.inflate(R.layout.checkbox_dependents,null);
            checkbox.setText(req.getTitulo());
            dependence = new Dependence(req.getId(), projeto.getId());

            dependence.setTitle(req.getTitulo());
            dependence.setDescription(req.getDescricao());
            checkbox.setTag(dependence);

            checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        dependences.add((Dependence) buttonView.getTag());
                    }else {
                        dependences.remove(buttonView.getTag());
                    }
                }
            });
            ll_container.addView(checkbox);
        }

    }
}
