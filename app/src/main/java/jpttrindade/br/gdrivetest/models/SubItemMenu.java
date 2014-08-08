package jpttrindade.br.gdrivetest.models;

/**
 * Created by joaotrindade on 06/08/14.
 */
public abstract class SubItemMenu {

    private String titulo;

    protected SubItemMenu(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
}
