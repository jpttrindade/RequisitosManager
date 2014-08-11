package jpttrindade.br.gdrivetest.models.repositorios;

import java.util.ArrayList;

/**
 * Created by jpttrindade on 10/08/14.
 */
public class QUERY {


    private final String WHERE = " WHERE ";
    private final String SELECT = "SELECT ";
    private final String FROM = " FROM ";

    private final String AND = " AND ";
    private final String OR = " OR ";

    private StringBuilder builder;

    private String table_name;

    private ArrayList<String> columns;
    private ArrayList<String> where_clauses;

    private String query = "";



    public QUERY(){
        columns = new ArrayList<String>();
        where_clauses = new ArrayList<String>();
    }

    public QUERY select(String... columns){

        query += SELECT;

        if(columns.length > 0){
            for(int i = 0; i<columns.length; i++){
                if(i==columns.length-1){
                    query += columns[i];
                }else {
                    query += columns[i]+", ";
                }
            }
        }else {
            query += "*";
        }

        return this;
    }

    public QUERY from(String table_name){
        query += FROM+table_name;
        return this;
    }

    public QUERY where(){
        query += WHERE;
        return this;
    }
    public QUERY whereClause(String field, String value){

        query += (field+"="+value);
        return this;
    }

    public QUERY and(){
        query += AND;
        return this;
    }

    public QUERY or(){
        query += OR;
        return this;
    }

    public QUERY openComplexClause(){
        query+="(";
        return  this;
    }

    public QUERY closeComplexClause(){
        query+=")";
        return  this;
    }

    public String finish(){
        return query+";";
    }











}
