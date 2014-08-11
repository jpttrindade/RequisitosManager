package jpttrindade.br.gdrivetest.models.repositorios;

/**
 * Created by jpttrindade on 09/08/14.
 */
public class SCRIPTS {

    public static String TABLE_PROJECT = "TB_Projects";
    public static String PROJECT_ID = "id_project";
    public static String PROJECT_TITLE = "title";
    public static String PROJECT_DESCRIPTION = "description";
    public static String PROJECT_MANAGER = "manager";
    public static String PROJECT_LASTREQUIREMENT = "last_requirement";

    public static String TABLE_REQUIREMENTS = "TB_Requirements";
    public static String REQUIREMENT_ID = "id_requirement";
    public static String REQUIREMENT_PROJECT_ID = "id_project";
    public static String REQUIREMENT_STATUS= "status";
    public static String REQUIREMENT_TITLE = "title";
    public static String REQUIREMENT_DESCRIPTION = "description";
    public static String REQUIREMENT_REQUESTER= "requester";
    public static String REQUIREMENT_DATE_CREATION= "date_creation";
    public static String REQUIREMENT_DATE_MODIFICATION= "date_modification";
    public static String REQUIREMENT_TYPE= "type";

    public static String TABLE_DEPENDENCE = "TB_Dependencies";
    public static String DEPENDENCE_ID_REQUIREMENT = "id_requirement";
    public static String DEPENDENCE_ID_PROJECT = "id_project";
    public static String DEPENDENCE_ID_DEPENDENT = "id_dependent";

    public static String[] CREATE = new String[]{
            "CREATE TABLE "+TABLE_PROJECT+"(" +
                    PROJECT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PROJECT_TITLE+" TEXT NOT NULL," +
                    PROJECT_DESCRIPTION+" TEXT NOT NULL," +
                    PROJECT_MANAGER+" TEXT NOT NULL," +
                    PROJECT_LASTREQUIREMENT+" INTEGER NOT NULL DEFAULT 1" +
            ");",

            "CREATE TABLE "+TABLE_REQUIREMENTS+"(" +
                    REQUIREMENT_ID+" INTEGER NOT NULL," +
                    REQUIREMENT_STATUS+" TEXT NOT NULL," +
                    REQUIREMENT_TITLE+" TEXT NOT NULL," +
                    REQUIREMENT_DESCRIPTION+" TEXT NOT NULL," +
                    REQUIREMENT_DATE_CREATION+" DATE NOT NULL," +
                    REQUIREMENT_DATE_MODIFICATION+" DATE NULL," +
                    REQUIREMENT_REQUESTER+" TEXT NOT NULL," +
                    REQUIREMENT_PROJECT_ID+" INTEGER NOT NULL," +
                    REQUIREMENT_TYPE+" INTEGER NOT NULL,"+
                    "PRIMARY KEY ("+REQUIREMENT_ID+", "+REQUIREMENT_PROJECT_ID+")," +
                    "FOREIGN KEY ("+REQUIREMENT_PROJECT_ID+")" +
                        "REFERENCES "+TABLE_PROJECT+"("+PROJECT_ID+")" +
            ");",

            "CREATE TABLE "+TABLE_DEPENDENCE+"(" +
                    DEPENDENCE_ID_REQUIREMENT+" INTEGER NOT NULL," +
                    DEPENDENCE_ID_PROJECT+" INTEGER NOT NULL," +
                    DEPENDENCE_ID_DEPENDENT+" INTEGER NOT NULL," +
                    "PRIMARY KEY ("+DEPENDENCE_ID_REQUIREMENT+", "+DEPENDENCE_ID_PROJECT+", "+DEPENDENCE_ID_DEPENDENT+")," +
                    "FOREIGN KEY ("+DEPENDENCE_ID_REQUIREMENT+", "+DEPENDENCE_ID_PROJECT+")" +
                        "REFERENCES "+TABLE_REQUIREMENTS+" ("+REQUIREMENT_ID+", "+REQUIREMENT_PROJECT_ID+")," +
                    "FOREIGN KEY ("+DEPENDENCE_ID_DEPENDENT+")" +
                        "REFERENCES "+TABLE_REQUIREMENTS+"("+REQUIREMENT_ID+")" +
            ");"

    };


}
