package jpttrindade.br.gdrivetest.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jpttrindade on 07/08/14.
 */
public class Requisito extends SubItemMenu implements Parcelable{

    private String id;
    private String descricao;
    private RequirementStatus status;

    private Date dataCriacao;
    private Date dataModificacao;

    private String requerente;

    private RequerimentType type;
    private Projeto projeto;
    private ArrayList<Dependence> dependentes;

    public Requisito(String titulo, String descricao, RequirementStatus status, RequerimentType type,Date dataCriacao,
                     Date dataModificacao, String requerente, Projeto projeto, ArrayList<Dependence> dependentes) {
        super(titulo);
        this.descricao = descricao;
        this.status = status;
        this.type = type;
        this.dataCriacao = dataCriacao;
        this.dataModificacao = dataModificacao;
        this.requerente = requerente;
        this.projeto = projeto;
        this.dependentes = dependentes;

    }


    public Requisito(String titulo, String id, String descricao, RequirementStatus status, RequerimentType type,Date dataCriacao,
                     Date dataModificacao, String requerente, Projeto projeto, ArrayList<Dependence> dependentes) {
        super(titulo);
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.type = type;
        this.dataCriacao = dataCriacao;
        this.dataModificacao = dataModificacao;
        this.requerente = requerente;
        this.projeto = projeto;
        this.dependentes = dependentes;
    }



    public Requisito(String titulo, String id, String descricao, RequirementStatus status, RequerimentType type,Date dataCriacao,
                     Date dataModificacao, String requerente, Projeto projeto) {
        super(titulo);
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.type = type;
        this.dataCriacao = dataCriacao;
        this.dataModificacao = dataModificacao;
        this.requerente = requerente;
        this.projeto = projeto;

    }

    public ArrayList<Dependence> getDependentes() {
        return dependentes;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public RequirementStatus getStatus() {
        return status;
    }

    public RequerimentType getType() {
        return type;
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

        this.id = in.readString();
        this.descricao = in.readString();
        this.status = RequirementStatus.setStatus(in.readString());
        this.type = RequerimentType.setType(in.readString());
        this.dataCriacao = new Date(in.readLong());//new SimpleDateFormat(SQLiteBDHelper.DATE_FORMAT).parse(in.readString());
        this.dataModificacao = new Date(in.readLong());//new SimpleDateFormat(SQLiteBDHelper.DATE_FORMAT).parse(in.readString());
        this.requerente = in.readString();
        this.projeto = (Projeto) in.readParcelable(Projeto.class.getClassLoader());
       this.dependentes = in.readArrayList(Requisito.class.getClassLoader());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getTitulo());
        dest.writeString(this.id);
        dest.writeString(this.descricao);
        dest.writeString(this.status.toString());
        dest.writeString(this.type.toString());
        dest.writeLong(this.dataCriacao.getTime());
        dest.writeLong(this.dataModificacao.getTime());
        dest.writeString(this.requerente);
        dest.writeParcelable(this.projeto, flags);
        dest.writeList(this.dependentes);
    }
}
