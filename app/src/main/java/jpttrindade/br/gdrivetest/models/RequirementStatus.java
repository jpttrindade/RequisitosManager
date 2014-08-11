package jpttrindade.br.gdrivetest.models;

/**
 * Created by jpttrindade on 07/08/14.
 */
public enum RequirementStatus {
    CONCLUIDO, ABERTO, STANDBY, ACORDADO, VALIDACAO;

    @Override
    public String toString(){

        switch (this) {
            case VALIDACAO:
                return "Validação";
            case ACORDADO:
                return "Acordado";
            case CONCLUIDO:
                return "Concluido";
            case STANDBY:
                return "Standby";
            default:
                return "Aberto";
        }
    }

    public static RequirementStatus setStatus(String sts) {
        return(sts.equals(RequirementStatus.ABERTO.toString()) ? RequirementStatus.ABERTO :
                (sts.equals(RequirementStatus.CONCLUIDO.toString()) ? RequirementStatus.CONCLUIDO :
                        RequirementStatus.STANDBY));
    }
}



