package jpttrindade.br.gdrivetest.views;

import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import jpttrindade.br.gdrivetest.models.Projeto;
import jpttrindade.br.gdrivetest.R;
import jpttrindade.br.gdrivetest.models.SQLiteBDHelper;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RESOLVE_CONECTION_REQUEST_CODE = 24;
    private static final  int CREATE_NEW_PROJECT = 777;
    public static final int ATT_PROJETO = 7771 ;
    private ExpandableListView listView;
    private GoogleApiClient mGoogleApiClient;
    private Button bt_newProject;

    private ListAdapter adapter;

    private SQLiteDatabase db;

    private Projeto projeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteBDHelper.getInstance(this);



        bt_newProject = (Button) findViewById(R.id.bt_newproject);
        bt_newProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(MainActivity.this, CreateNewProjectActivity.class), CREATE_NEW_PROJECT);
            }
        });

        listView = (ExpandableListView) findViewById(R.id.listview);
        adapter = new ListAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent it = new Intent(MainActivity.this, RequirementsActivity.class);
                projeto = (Projeto)v.getTag();
                it.putExtra("projeto", projeto);
                startActivityForResult(it, ATT_PROJETO);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();*/
    }


    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESOLVE_CONECTION_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    mGoogleApiClient.connect();
                }
                break;
            case CREATE_NEW_PROJECT:

                switch (resultCode){
                    case RESULT_OK:
                        Toast.makeText(this, "Projeto criado com sucesso!", Toast.LENGTH_SHORT).show();
                        adapter.addSubItemMenu(ListAdapter.OPTION_MENU_PROJETOS, (Projeto)data.getParcelableExtra("projeto"));
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Projeto cancelado!", Toast.LENGTH_SHORT).show();
                        break;
                }

                break;
            case ATT_PROJETO:
                switch (resultCode){
                    case RESULT_OK:

                        adapter.attProjetos();
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()){
            try{
                connectionResult.startResolutionForResult(this, RESOLVE_CONECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }
}
