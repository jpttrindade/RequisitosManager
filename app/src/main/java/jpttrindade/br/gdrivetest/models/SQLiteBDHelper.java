package jpttrindade.br.gdrivetest.models;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import jpttrindade.br.gdrivetest.views.CreateBDAsyncTask;

/**
 * Created by joaotrindade on 07/08/14.
 */
public class SQLiteBDHelper extends SQLiteOpenHelper {


    public static final String TABLE_REQUISITOS = "tb_requisitos";
    public static final String REQUISITOS_ID = "id_requisito";
    public static final String REQUISITOS_STATUS = "status";
    public static final String REQUISITOS_TITULO = "titulo";
    public static final String REQUISITOS_DESCRICAO= "descricao";
    public static final String REQUISITOS_DATACRIACAO = "dataCricacao";
    public static final String REQUISITOS_DATAMODIFICACAO = "dataModificacao";
    public static final String REQUISITOS_REQUERENTE = "requerente";
    public static final String REQUISITOS_ID_PROJETO = "tb_projeto_id_projeto";

    public static final String DATE_FORMAT = "EEEE, dd/MM/yyyy/hh:mm:ss";

    public static final String TABLE_PROJETO = "tb_projeto";
    public static final String PROJETO_ID = "id_projeto";
    public static final String PROJETO_TITULO = "titulo";
    public static final String PROJETO_DESCRICAO = "descricao";
    public static final String PROJETO_GERENTE = "gerente";
    public static final String PROJETO_TOTAL_REQUISITOS = "totalRequisitos";

    private static final String DATABASE_NAME = "gdrivebd";
    private static final int DATABASE_VERSION = 1 ;


    private ArrayList<String> CREATE;

    private static SQLiteBDHelper singleton;
    Context mContext;


    private SQLiteBDHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context.getApplicationContext();
     //   CREATE = createScript;
    }

    public static SQLiteBDHelper getInstance(Context context){
        if(singleton == null){
            singleton = new SQLiteBDHelper(context);
        }

        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        new CreateBDAsyncTask(mContext, db).execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
