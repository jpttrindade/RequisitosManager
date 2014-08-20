package jpttrindade.br.gdrivetest.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joaotrindade on 11/08/14.
 */
public class Dependence implements Parcelable{

    private String id_parent;
    private String id_project;
    private String id_child;
    private String description;
    private String title;

    public Dependence(String id_parent, String id_project, String id_child) {
        this.id_parent = id_parent;
        this.id_project = id_project;
        this.id_child = id_child;
    }

    public Dependence(String id_parent, String id_project) {
        this.id_project = id_project;
        this.id_parent = id_parent;
    }

    public void setId_child(String id_child) {
        this.id_child = id_child;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId_parent() {
        return id_parent;
    }

    public String getId_project() {
        return id_project;
    }

    public String getId_child() {
        return id_child;
    }

    public String getDescription() {
        return description;
    }


    public static Parcelable.Creator<Dependence> CREATOR = new Parcelable.Creator<Dependence>(){

        @Override
        public Dependence createFromParcel(Parcel source) {
            return new Dependence(source);
        }

        @Override
        public Dependence[] newArray(int size) {
            return new Dependence[size];
        }
    };

    private Dependence(Parcel in){
        this.id_parent = in.readString();
        this.id_project = in.readString();
        this.id_child = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_parent);
        dest.writeString(id_project);
        dest.writeString(id_child);
    }


}
