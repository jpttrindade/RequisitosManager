package jpttrindade.br.gdrivetest.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.Requisito;
import jpttrindade.br.gdrivetest.models.Status;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioRequirements;

/**
 * Created by jpttrindade on 08/08/14.
 */
public class CreateNewRequirementActivity  extends Activity{

    private Spinner spinnerStatus;
    private Button create, cancel;

    private TextView titulo, descricao, requerente;

    private Date dataCricacao;
    private Status status;

    private Projeto projeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewrequirement);

        projeto = (Projeto) getIntent().getParcelableExtra("projeto");
        create = (Button) findViewById(R.id.bt_create);
        cancel = (Button) findViewById(R.id.bt_cancel);

        titulo = (TextView) findViewById(R.id.et_titulo);
        descricao = (TextView) findViewById(R.id.et_descricao);
        requerente = (TextView) findViewById(R.id.et_requerente);

        spinnerStatus = (Spinner) findViewById(R.id.spinner_status);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              String sts =  ((TextView)view).getText().toString();
              status = (sts.equals(Status.ABERTO.getStatus()) ? Status.ABERTO :
                        (sts.equals(Status.CONCLUIDO.getStatus()) ? Status.CONCLUIDO :
                                Status.STANDBY));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Requisito> dependentes = new ArrayList<Requisito>();
                dataCricacao = Calendar.getInstance().getTime();
                String id = projeto.getIdNextRequirements()+"";

                Log.d("DEBUG", "id = "+id);
                Requisito nRequisito = new Requisito(titulo.getText().toString(), id, descricao.getText().toString(), status, dataCricacao, dataCricacao,
                                                     requerente.getText().toString(), projeto, dependentes);


                RepositorioRequirements.getInstance(CreateNewRequirementActivity.this).addRequirement(nRequisito);

                Intent it = new Intent();
                it.putExtra("requirement", nRequisito);
                it.putExtra("projeto", projeto);
                setResult(RESULT_OK, it);
                finish();
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
}
