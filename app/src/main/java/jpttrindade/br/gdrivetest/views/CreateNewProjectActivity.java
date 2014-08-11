package jpttrindade.br.gdrivetest.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.repositorios.RepositorioProjetos;

/**
 * Created by joaotrindade on 06/08/14.
 */
public class CreateNewProjectActivity extends Activity {

    Button bt_create, bt_cancel;

    EditText et_titulo, et_descricao, et_gerente;



    private void initialize() {
        et_titulo = (EditText) findViewById(R.id.et_titulo);
        et_descricao = (EditText) findViewById(R.id.et_descricao);
        et_gerente = (EditText) findViewById(R.id.et_gerente);

        bt_create = (Button) findViewById(R.id.bt_create);

        bt_cancel = (Button) findViewById(R.id.bt_cancel);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewproject);

        initialize();

        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Projeto nProjeto = new Projeto(et_titulo.getText().toString(), et_descricao.getText().toString(), et_gerente.getText().toString());

                RepositorioProjetos.getInstance(CreateNewProjectActivity.this)
                        .addProjeto(nProjeto);



                Intent it = new Intent();
                it.putExtra("projeto", nProjeto);
                setResult(RESULT_OK, it);
                finish();
            }
        });




        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
    }

}
