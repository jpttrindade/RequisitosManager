package jpttrindade.br.gdrivetest.views;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.Requisito;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioProjetos;

public class RequirementsActivity extends ActionBarActivity {

    private static final  int CREATE_NEW_REQUIREMENT = 888;
    private static final  int EDIT_REQUIREMENT = 999;

    private Button bt_newRequirement;
    private ExpandableListView lv_requirements;
    private ArrayList<Requisito> requirements = new ArrayList<Requisito>();
    private RequirementsAdapter adapter;
    private Projeto projeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements);

        bt_newRequirement = (Button) findViewById(R.id.bt_newRequirement);
        lv_requirements = (ExpandableListView) findViewById(R.id.lv_requirements);

        projeto = (Projeto) getIntent().getParcelableExtra("projeto");

        adapter = new RequirementsAdapter(this, projeto, requirements);

        requirements = adapter.getRequirements();

        lv_requirements.setAdapter(adapter);

        bt_newRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(RequirementsActivity.this, CreateNewRequirementActivity.class);
                it.putExtra("projeto", projeto);
                it.putParcelableArrayListExtra("requirements", requirements);
                startActivityForResult(it, CREATE_NEW_REQUIREMENT);
            }
        });


    }

    public void startEditRequirementActivity(Requisito requisito){
        Intent it = new Intent(this, EditRequirementActivity.class);
        it.putExtra("requirement", requisito);
        it.putParcelableArrayListExtra("requirements", requirements);
        adapter.removeRequirement(requisito);
        startActivityForResult(it, EDIT_REQUIREMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CREATE_NEW_REQUIREMENT:
                switch (resultCode){
                    case RESULT_OK:
                        Toast.makeText(this, "Requisito criado com sucesso!", Toast.LENGTH_LONG).show();
                        Requisito newRequirement = (Requisito)data.getParcelableExtra("requirement");

                      //  Log.d("DEBUG", "novo requisito_id = " + newRequirement.getId());

                        adapter.addRequirement(newRequirement);
                        projeto = (Projeto) data.getParcelableExtra("projeto");

                        RepositorioProjetos.getInstance(RequirementsActivity.this).updateProjeto(projeto);
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Requisito cancelado!", Toast.LENGTH_LONG).show();
                        break;

                    default:
                }
                break;

            case EDIT_REQUIREMENT:
                Requisito requisito;
                switch (resultCode){
                    case RESULT_OK:
                        Toast.makeText(this, "Requisito editado com sucesso!", Toast.LENGTH_LONG).show();
                        requisito = (Requisito) data.getParcelableExtra("requirement");

                        adapter.addRequirement(requisito);

                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Edição cancelada!", Toast.LENGTH_LONG).show();
                        requisito = (Requisito) data.getParcelableExtra("requirement");
                        adapter.addRequirement(requisito);
                        break;
                    default:
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {

        RepositorioProjetos.getInstance(this).updateProjeto(projeto);
        setResult(RESULT_OK);
        super.onBackPressed();
        finish();

    }
}
