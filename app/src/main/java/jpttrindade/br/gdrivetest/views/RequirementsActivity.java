package jpttrindade.br.gdrivetest.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.models.Requirement;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioProjetos;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioRequirements;

public class RequirementsActivity extends ActionBarActivity {

    private static final  int CREATE_NEW_REQUIREMENT = 888;
    private static final  int EDIT_REQUIREMENT = 999;
    public static final int RESULT_REQUIREMENT_DELETED = 123456;

    private Button bt_newRequirement;
    private ExpandableListView lv_requirements;
    private ArrayList<Requirement> requirements = new ArrayList<Requirement>();
    private RequirementsAdapter adapter;
    private Projeto projeto;
    private Requirement req_deleted;

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

        lv_requirements.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int itemType = ExpandableListView.getPackedPositionType(id);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

                    return false;

                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    //int groupPosition = ExpandableListView.getPackedPositionGroup(id);

                    String id_requirement = (String) view.getTag();

                    req_deleted = null;
                    for (Requirement req : requirements) {
                        if (req.getId().equals(id_requirement)) {
                            req_deleted = req;
                        }
                    }

                    AlertDialog dialog = new AlertDialog.Builder(RequirementsActivity.this).setTitle("Delete "+req_deleted.getTitulo())
                            .setMessage("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RepositorioRequirements.getInstance(RequirementsActivity.this)
                                            .deleteRequirement(req_deleted);
                                    adapter.removeRequirement(req_deleted);
                                    adapter.attAdapter();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();

                    return true;

                } else {
                    // null item; we don't consume the click
                    return false;
                }
            }
        });

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

    public void startEditRequirementActivity(Requirement requirement){
        Intent it = new Intent(this, EditRequirementActivity.class);
        it.putExtra("requirement", requirement);
        it.putParcelableArrayListExtra("requirements", requirements);
        //adapter.removeRequirement(requirement);
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
                        Requirement newRequirement = (Requirement)data.getParcelableExtra("requirement");

                        adapter.addRequirement(newRequirement);
                        requirements = adapter.getRequirements();
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
                Requirement requirement;
                switch (resultCode){
                    case RESULT_OK:
                        Toast.makeText(this, "Requisito editado com sucesso!", Toast.LENGTH_LONG).show();
                        requirement = (Requirement) data.getParcelableExtra("requirement");

                        adapter.addRequirement(requirement);
                        adapter.attAdapter();

                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Edição cancelada!", Toast.LENGTH_LONG).show();
                        //requirement = (Requirement) data.getParcelableExtra("requirement");
                       // adapter.addRequirement(requirement);
                        break;

                    case RESULT_REQUIREMENT_DELETED:
                        Toast.makeText(this, "Requirement deleted!", Toast.LENGTH_LONG).show();
                        String id_parent = data.getStringExtra("id_reqDeleted");
                        adapter.attAdapter();


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
