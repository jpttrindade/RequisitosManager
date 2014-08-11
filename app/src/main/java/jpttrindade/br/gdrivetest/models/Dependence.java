package jpttrindade.br.gdrivetest.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joaotrindade on 11/08/14.
 */
public class Dependence implements Parcelable {

    private String id_requirement;
    private String id_project;
    private String id_dependent;
    private String description;
    private String title;

    public Dependence(String id_requirement, String id_project, String id_dependent) {
        this.id_requirement = id_requirement;
        this.id_project = id_project;
        this.id_dependent = id_dependent;
    }

    public Dependence(String id_project, String id_dependent) {
        this.id_project = id_project;
        this.id_dependent = id_dependent;
    }

    public void setId_requirement(String id_requirement) {
        this.id_requirement = id_requirement;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId_requirement() {
        return id_requirement;
    }

    public String getId_project() {
        return id_project;
    }

    public String getId_dependent() {
        return id_dependent;
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
        this.id_requirement = in.readString();
        this.id_project = in.readString();
        this.id_dependent = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_requirement);
        dest.writeString(id_project);
        dest.writeString(id_dependent);
    }
}
