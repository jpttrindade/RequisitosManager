package jpttrindade.br.gdrivetest.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.RequerimentType;
import jpttrindade.br.gdrivetest.models.RequirementStatus;
import jpttrindade.br.gdrivetest.models.Requisito;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioDependences;
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
    private ArrayList<Requisito> requirements;
    private ArrayList<Requisito> dependentes;

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

        dependentes = new ArrayList<Requisito>();
        ll_container = (LinearLayout) findViewById(R.id.ll_container);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewrequirement);


        initialize();

        new DependetsAsyncTask().execute(projeto);

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
                    String id = projeto.getIdNextRequirements() + "";

                    //Log.d("DEBUG", "id = "+id);
                    Requisito nRequisito = new Requisito(titulo, id, descricao, status, type, dataCricacao, dataCricacao,
                            requerente, projeto, dependentes);

                    RepositorioRequirements.getInstance(CreateNewRequirementActivity.this).addRequirement(nRequisito);


                    Intent it = new Intent();
                    it.putExtra("requirement", nRequisito);
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

    private class DependetsAsyncTask extends AsyncTask<Projeto, Void, Void>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(CreateNewRequirementActivity.this, "","wait...");
        }

        @Override
        protected Void doInBackground(Projeto... params) {
            try {
                requirements = RepositorioRequirements.getInstance(CreateNewRequirementActivity.this).getRequisitos(projeto);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LayoutInflater inflater = LayoutInflater.from(CreateNewRequirementActivity.this);
            CheckBox checkbox = null;
            for(Requisito req : requirements){
                checkbox = (CheckBox) inflater.inflate(R.layout.checkbox_dependents,null);
                checkbox.setText(req.getTitulo());
                checkbox.setTag(req);

                checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            dependentes.add((Requisito)buttonView.getTag());
                        }else {
                            dependentes.remove((Requisito)buttonView.getTag());
                        }
                    }
                });
                ll_container.addView(checkbox);
            }

            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
}
