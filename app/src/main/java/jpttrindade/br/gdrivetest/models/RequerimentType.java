package jpttrindade.br.gdrivetest.models;

/**
 * Created by jpttrindade on 09/08/14.
 */
public enum RequerimentType {


    RF, RNF;

    @Override
    public String toString(){
        String s = "";
        switch (this){
            case RF:
                s = "Funcional";
                break;
            case RNF:
                s = "Não-Funcional";
                break;
        }

        return s;

    }

    public static RequerimentType setType(String tp){
        return (tp.equals(RF.toString()) ? RF :
                RNF);
    }
}
