package jpttrindade.br.gdrivetest.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joaotrindade on 06/08/14.
 */

public class Projeto extends SubItemMenu implements Parcelable{


    private String id;
    private String descricao;
    private String gerente;



    private int idNextRequirements;



    public Projeto(String  titulo, String descricao, String gerente) {
        super(titulo);
        this.descricao = descricao;
        this.gerente = gerente;

        this.idNextRequirements = 1;

    }

    public Projeto(String id, String titulo, String descricao, String gerente, int idNextRequirements) {
        super(titulo);
        this.id = id;
        this.descricao = descricao;
        this.gerente = gerente;
        this.idNextRequirements = idNextRequirements;
    }

    public String getId() {
        return id;
    }

    public int getIdNextRequirements() {
        int retorno = idNextRequirements;
        idNextRequirements = idNextRequirements + 1;
        return retorno;
    }

    public void setIdNextRequirements(int idNextRequirements) {
        this.idNextRequirements = idNextRequirements;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getGerente() {
        return gerente;
    }



    public void setId(String id) {
        this.id = id;
    }



    public static Parcelable.Creator<Projeto>  CREATOR = new Parcelable.Creator<Projeto>(){

        @Override
        public Projeto createFromParcel(Parcel source) {
            return new Projeto(source);
        }

        @Override
        public Projeto[] newArray(int size) {
            return new Projeto[size];
        }
    };



     private Projeto(Parcel in){

         super(in.readString());
         this.id = in.readString();
         this.descricao = in.readString();
         this.gerente = in.readString();
         this.idNextRequirements = in.readInt();

     }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(getTitulo());

        dest.writeString(this.id);
        dest.writeString(this.descricao);
        dest.writeString(this.gerente);
        dest.writeInt(this.idNextRequirements);


    }
}
