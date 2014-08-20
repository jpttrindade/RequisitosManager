package jpttrindade.br.gdrivetest.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jpttrindade on 07/08/14.
 */
public class Requirement extends SubItemMenu implements Parcelable{

    private String id;
    private String descricao;
    private RequirementStatus status;

    private Date dataCriacao;
    private Date dataModificacao;

    private String requerente;



    private RequerimentType type;
    private Projeto projeto;
    private ArrayList<Dependence> dependences;

    private final ArrayList<Dependence> removedDependets = new ArrayList<Dependence>();
    private final ArrayList<Dependence> newDependents = new ArrayList<Dependence>();



    public Requirement(String titulo, String id, String descricao, RequirementStatus status, RequerimentType type, Date dataCriacao,
                       Date dataModificacao, String requerente, Projeto projeto, ArrayList<Dependence> dependentes) {
        this(titulo, id, descricao, status, type, dataCriacao, dataModificacao, requerente, projeto);
        this.dependences = dependentes;
    }



    public Requirement(String titulo, String id, String descricao, RequirementStatus status, RequerimentType type, Date dataCriacao,
                       Date dataModificacao, String requerente, Projeto projeto) {
        this(titulo,descricao,status, type, dataCriacao, dataModificacao, requerente, projeto);
        this.id = id;
    }

    private Requirement(String titulo, String descricao, RequirementStatus status, RequerimentType type, Date dataCriacao,
                        Date dataModificacao, String requerente, Projeto projeto ){
        super(titulo);
        this.descricao = descricao;
        this.status = status;
        this.type = type;
        this.dataCriacao = dataCriacao;
        this.dataModificacao = dataModificacao;
        this.requerente = requerente;
        this.projeto = projeto;

    }

    public ArrayList<Dependence> getDependences() {
        return dependences;
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


    public void addNewDependence(Dependence dependence){
        if(removedDependets.contains(dependence)){
            removedDependets.remove(dependence);
        }else {
            newDependents.add(dependence);
        }

    }

    public ArrayList<Dependence> getNewDependents(){

        ArrayList<Dependence> retorno = new ArrayList<Dependence>();

        retorno.addAll(newDependents);

        dependences.addAll(newDependents);

        newDependents.clear();


        return retorno;
    }


    public void addRemovedDependence(Dependence dependence){
        for(int i = 0; i < dependences.size(); i++){
            if(dependences.get(i).getId_child().equals(dependence.getId_child())){
                dependences.remove(i);
                if(newDependents.contains(dependence)){
                    newDependents.remove(dependence);
                } else{
                    removedDependets.add(dependence);
                }
            }
        }
    }

    public ArrayList<Dependence> getRemovedDependets(){
        ArrayList<Dependence> retorno = new ArrayList<Dependence>();

        retorno.addAll(removedDependets);

        boolean limpou = dependences.removeAll(removedDependets);



        Log.d("DEBUG", "limpou="+limpou);

        removedDependets.clear();

        return  retorno;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(RequirementStatus status) {
        this.status = status;
    }

    public void setDataModificacao(Date dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public void setRequerente(String requerente) {
        this.requerente = requerente;
    }

    public void setType(RequerimentType type) {
        this.type = type;
    }


    public static Parcelable.Creator<Requirement> CREATOR =
            new Parcelable.Creator<Requirement>(){

                @Override
                public Requirement createFromParcel(Parcel source) {
                    return new Requirement(source);
                }

                @Override
                public Requirement[] newArray(int size) {
                    return new Requirement[size];
                }
            };


    private Requirement(Parcel in){
        super(in.readString());

        this.id = in.readString();
        this.descricao = in.readString();
        this.status = RequirementStatus.setStatus(in.readString());
        this.type = RequerimentType.setType(in.readString());
        this.dataCriacao = new Date(in.readLong());//new SimpleDateFormat(SQLiteBDHelper.DATE_FORMAT).parse(in.readString());
        this.dataModificacao = new Date(in.readLong());//new SimpleDateFormat(SQLiteBDHelper.DATE_FORMAT).parse(in.readString());
        this.requerente = in.readString();
        this.projeto = (Projeto) in.readParcelable(Projeto.class.getClassLoader());
       this.dependences = in.readArrayList(Requirement.class.getClassLoader());

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
        dest.writeList(this.dependences);
    }
}
