package jpttrindade.br.gdrivetest.models;

/**
 * Created by jpttrindade on 07/08/14.
 */
public enum Status {
    CONCLUIDO, ABERTO, STANDBY;

    public String getStatus(){
        switch (this) {
            case CONCLUIDO:
                return "Concluido";
            case STANDBY:
                return "Standby";
            default:
                return "Aberto";
        }
    }
}



