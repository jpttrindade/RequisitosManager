package jpttrindade.br.gdrivetest.models;

import java.util.ArrayList;

/**
 * Created by joaotrindade on 05/08/14.
 */
public class ItemMenu {
    private String nome;
    private ArrayList<SubItemMenu> subItens;

    

    public ItemMenu(String nomeEquipe) {
        nome = nomeEquipe;
        subItens = new ArrayList<SubItemMenu>();
    }

    public ItemMenu addSubItem(SubItemMenu item){
        subItens.add(item);
        return this;
    }

    public ArrayList<SubItemMenu> getSubItens() {
        return subItens;
    }

    public String getNome() {
        return nome;
    }
}
