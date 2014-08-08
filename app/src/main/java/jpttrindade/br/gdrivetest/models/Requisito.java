package jpttrindade.br.gdrivetest.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jpttrindade on 07/08/14.
 */
public class Requisito extends SubItemMenu implements Parcelable{

    private String id;
    private String descricao;
    private Status status;

    private Date dataCriacao;
    private Date dataModificacao;

    private String requerente;
    private Projeto projeto;
    private ArrayList<Requisito> dependentes;

    public Requisito(String titulo, String descricao, Status status, Date dataCriacao, Date dataModificacao,
                     String requerente, Projeto projeto, ArrayList<Requisito> dependentes) {
        super(titulo);
        this.descricao = descricao;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataModificacao = dataModificacao;
        this.requerente = requerente;
        this.projeto = projeto;
        this.dependentes = dependentes;
    }

    public Requisito(String titulo, String id, String descricao, Status status, Date dataCriacao,
                     Date dataModificacao, String requerente, Projeto projeto, ArrayList<Requisito> dependentes) {
        super(titulo);
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataModificacao = dataModificacao;
        this.requerente = requerente;
        this.projeto = projeto;
        this.dependentes = dependentes;
    }

    public ArrayList<Requisito> getDependentes() {
        return dependentes;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Status getStatus() {
        return status;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public Date getDataModificacao() {
        return dataModificacao;
    }

    public String getRequerente() {
        return requerente;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public static Parcelable.Creator<Requisito> CREATOR =
            new Parcelable.Creator<Requisito>(){

                @Override
                public Requisito createFromParcel(Parcel source) {
                    return new Requisito(source);
                }

                @Override
                public Requisito[] newArray(int size) {
                    return new Requisito[size];
                }
            };

    private Requisito(Parcel in){
        super(in.readString());
            this.descricao = in.readString();
            String sts = in.readString();
            this.status = (sts.equals(Status.ABERTO.getStatus()) ? Status.ABERTO :
                    (sts.equals(Status.CONCLUIDO.getStatus()) ? Status.CONCLUIDO :
                            Status.STANDBY));

            this.dataCriacao = new Date(in.readLong());//new SimpleDateFormat(SQLiteBDHelper.DATE_FORMAT).parse(in.readString());
            this.dataModificacao = new Date(in.readLong());//new SimpleDateFormat(SQLiteBDHelper.DATE_FORMAT).parse(in.readString());
            this.requerente = in.readString();
            this.projeto = (Projeto) in.readParcelable(Projeto.class.getClassLoader());
            in.readList(this.dependentes, Requisito.class.getClassLoader());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getTitulo());
        dest.writeString(this.descricao);
        dest.writeString(this.status.getStatus());
        dest.writeLong(this.dataCriacao.getTime());
        dest.writeLong(this.dataModificacao.getTime());
        dest.writeString(this.requerente);
        dest.writeParcelable(this.projeto, flags);
        dest.writeTypedList(this.dependentes);
    }
}
